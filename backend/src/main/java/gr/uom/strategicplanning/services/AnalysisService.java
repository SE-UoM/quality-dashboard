package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GitClient;
import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalysis;
import gr.uom.strategicplanning.analysis.refactoringminer.RefactoringMinerAnalysis;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.controllers.responses.implementations.ErrorResponse;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.exceptions.AnalysisException;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.models.users.User;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AnalysisService {
    private final SonarApiClient sonarApiClient;
    private final GithubApiClient githubApiClient;
    private final GithubService githubService;
    private final CommitService commitService;
    private final ProjectService projectService;
    private final UserService userService;
    private SonarAnalysis sonarAnalysis;

    @Value("${sonar.sonarqube.url}")
    private String SONARQUBE_URL;

    @Value("${services.external.activated}")
    private boolean EXTERNAL_ANALYSIS_IS_ACTIVATED;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private ProjectStatsService projectStatsService;

    @Autowired
    private CodeSmellDistributionService codeSmellDistributionService;

    @Autowired
    private OrganizationAnalysisService organizationAnalysisService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ExternalAnalysisService externalAnalysisService;

    @Autowired
    public AnalysisService(
            CommitService commitService,
            @Value("${github.token}") String githubToken,
            @Value("${sonar.sonarqube.url}") String sonarApiUrl,
            ProjectService projectService,
            UserService userService,
            GithubService githubService
    ) {
        this.commitService = commitService;
        this.projectService = projectService;
        this.githubApiClient = new GithubApiClient(githubToken);
        this.sonarApiClient = new SonarApiClient(sonarApiUrl);
        this.userService = userService;
        this.githubService = githubService;
    }

    public void fetchGithubData(Project project) throws Exception {
        Map<String, Object> githubData = githubApiClient.fetchProjectData(project);

        project.setProjectDescription((String) githubData.get("description"));
        project.setDefaultBranchName((String) githubData.get("defaultBranch"));
        project.setForks((int) githubData.get("totalForks"));
        project.setStars((int) githubData.get("totalStars"));
        project.setTotalCommits((int) githubData.get("totalCommits"));

        projectService.saveProject(project);
    }

    public boolean validateUrlWithGithub(String url) {
        return githubApiClient.repoFoundByGithubAPI(url);
    }

    private void analyzeCommits(Project project) throws Exception {
        List<String> commitList = GitClient.fetchCommitSHAsList(project);

        List<String> commitListFinal = new ArrayList<>();
        for (String sha: commitList){
            if(project.getCommits().stream().anyMatch(x-> Objects.equals(x.getHash(), sha)))
                continue;
            commitListFinal.add(sha);
        }
        Collections.reverse(commitListFinal);

        for (String commitSHA : commitListFinal) {
            System.out.println("Analyzing " + commitListFinal.indexOf(commitSHA)+1 + " out of " + commitListFinal.size() + " commits");
            GitClient.checkoutCommit(project, commitSHA);

            Commit commit = new Commit();
            commit.setHash(commitSHA);

            sonarAnalysis = new SonarAnalysis(project, commitSHA, SONARQUBE_URL);

            commitService.populateCommit(commit, project);
            project.addCommit(commit);
        }
    }

    private void analyzeMaster(Project project) throws Exception {
        GitClient.checkoutMaster(project);

        sonarAnalysis = new SonarAnalysis(project, project.getDefaultBranchName(), SONARQUBE_URL);
    }

    public Project analyzeProject(String url, User user) throws Exception {
       log.info("AnalysisService - analyzeProject - url: " + url + " - user: " + user.getEmail());

        boolean urlIsInvalid = url==null || url.isEmpty() || !url.matches("https://github.com/[^/]+/[^/]+" );

        if (urlIsInvalid || !validateUrlWithGithub(url)) {
            log.error("AnalysisService - analyzeProject - Invalid github url");
            throw new AnalysisException(HttpStatus.BAD_REQUEST, "Invalid github url");
        }

        Organization organization = user.getOrganization();
        Project project = projectService.getOrCreateProject(url, organization);

        if (project.getStatus() == ProjectStatus.ANALYSIS_STARTED || project.getStatus() == ProjectStatus.ANALYSIS_IN_PROGRESS) {
            log.error("AnalysisService - analyzeProject - The project is already being analyzed");
            throw new AnalysisException(HttpStatus.BAD_REQUEST, "The project is already being analyzed");
        }

        if (project.getStatus() == ProjectStatus.ANALYSIS_TO_BE_REVIEWED) {
            log.error("AnalysisService - analyzeProject - The project has more commits than the threshold and needs to be reviewed");
            throw new AnalysisException(HttpStatus.BAD_REQUEST, "The project needs to be reviewed by an Admin first.");
        }

        Map<String, Object> githubData = githubApiClient.fetchProjectData(project);

        project.setProjectDescription((String) githubData.get("description"));
        project.setDefaultBranchName((String) githubData.get("defaultBranch"));
        project.setForks((int) githubData.get("totalForks"));
        project.setStars((int) githubData.get("totalStars"));
        project.setTotalCommits((int) githubData.get("totalCommits"));

        projectService.saveProject(project);

        if (!project.hasLessCommitsThanThreshold()) {
            log.error("AnalysisService - analyzeProject - The project has more commits than the threshold and needs to be reviewed");
            throw new AnalysisException(HttpStatus.BAD_REQUEST, "The project has a lot of Commits.");
        }

        Integer analyzedCommits = startAnalysis(project);

        if (analyzedCommits == 0) {
            log.info("AnalysisService - analyzeProject - All commits are already analyzed, you can see the results on the dashboard");
            throw new AnalysisException(HttpStatus.OK, "All commits are already analyzed, you can see the results on the dashboard.");
        }

        log.info("AnalysisService - analyzeProject - Analysis Finished! " + analyzedCommits + " commits analyzed");

        organizationAnalysisService.updateOrganizationAnalysis(organization);
        organizationService.saveOrganization(organization);

        log.info("AnalysisService - analyzeProject - Organization Analysis Updated");
        log.info("AnalysisService - analyzeProject - External Analysis Activated: " + EXTERNAL_ANALYSIS_IS_ACTIVATED);

        if (EXTERNAL_ANALYSIS_IS_ACTIVATED) {
            log.info("AnalysisService - analyzeProject - External Analysis Started");
             externalAnalysisService.analyzeWithExternalServices(project);
        }

        log.info("AnalysisService - analyzeProject - External Analysis Finished");

        return project;
    }

    private void extractAnalysisDataForProject(Project project) throws Exception {
        Collection languages = languageService.extractLanguagesFromProject(project);

        // If the project has no languages try again 5 times waiting 5 seconds between each try
        int tries = 0;
        while (languages.isEmpty() && tries < 5) {
            System.out.println("No languages found, trying again (" + tries + "/5)");
            Thread.sleep(5000);
            languages = languageService.extractLanguagesFromProject(project);
            tries++;
        }

        // If the project still has no languages, throw an exception
        if (languages.isEmpty()) {
            throw new Exception("No languages found for project " + project.getName());
        }

        int totalLanguages = languages.size();
        int totalDevelopers = project.getDevelopers().size();

        Collection<ProjectCodeSmellDistribution> codeSmellsDistribution = sonarApiClient.fetchCodeSmellsDistribution(project);
        codeSmellDistributionService.saveCollectionOfCodeSmellDistribution(codeSmellsDistribution);

        ProjectStats projectStats = project.getProjectStats();
        projectStats.setCodeSmellDistributions(codeSmellsDistribution);
        projectStatsService.saveProjectStats(projectStats);

        project.setTotalDevelopers(totalDevelopers);
        project.setTotalLanguages(totalLanguages);
        project.setLanguages(languages);

        Collection<Developer> developers = project.getDevelopers();
        for (Developer developer : developers) {
            developer.setTotalCommits(0);
            developer.setTotalCodeSmells(0);
            developer.setCodeSmellsPerCommit(0);
        }

        for (Commit commit : project.getCommits()) {
            Developer developer = commit.getDeveloper();
            developer.setTotalCommits(developer.getTotalCommits() + 1);

            int totalCodeSmells = commit.getCodeSmells().size();
            developer.setTotalCodeSmells(totalCodeSmells);

            double codeSmellsPerCommit = (double) totalCodeSmells / developer.getTotalCommits();
            developer.setCodeSmellsPerCommit(codeSmellsPerCommit);
        }

        projectService.populateProjectStats(project);
    }

    public Integer startAnalysis(Project project) throws Exception {
        Git clonedGit = GitClient.cloneRepository(project);

        try {
            String sha = GitClient.getShaOfClonedProject(clonedGit);

            if (project.commitsAlreadyAnalyzed(sha)){
                project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
                clonedGit.close();
                GitClient.deleteRepository(project);

                return 0;
            }

            project.setStatus(ProjectStatus.ANALYSIS_STARTED);
            projectService.saveProject(project);

            String defaultBranch = project.getDefaultBranchName();
            RefactoringMinerAnalysis refactoringMinerAnalysis = new RefactoringMinerAnalysis(project.getRepoUrl(), defaultBranch, project.getName());
            project.setTotalRefactorings(refactoringMinerAnalysis.getTotalNumberOfRefactorings());

            analyzeCommits(project);

            extractAnalysisDataForProject(project);

            // Set the total commits of the project
            project.setTotalCommits(project.getCommits().size());

            project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
            // Save the project with updated analysis data
            projectService.saveProject(project);

            clonedGit.close();
            GitClient.deleteRepository(project);

            return project.getCommits().size();
        } catch (Exception e) {
            System.out.println("Analysis failed for project " + project.getName());
            e.printStackTrace();

            clonedGit.close();
            GitClient.deleteRepository(project);

            project.setStatus(ProjectStatus.ANALYSIS_FAILED);
            projectService.saveProject(project);

            // The AnalysisExceptionHandler will catch this exception and return an appropriate response
            throw new AnalysisException(HttpStatus.INTERNAL_SERVER_ERROR, "Analysis failed. Please try again later or contact support.");
        }
    }

}
