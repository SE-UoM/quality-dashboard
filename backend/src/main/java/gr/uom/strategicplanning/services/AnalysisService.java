package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GitClient;
import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalysis;
import gr.uom.strategicplanning.analysis.refactoringminer.RefactoringMinerAnalysis;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.exceptions.AnalysisException;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.models.users.User;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;


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
    private final ProjectValidationService projectValidationService;

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

    public static final int COMMITS_ANALYZED_ALREADY_FLAG = -1;

    @Autowired
    public AnalysisService(
            CommitService commitService,
            @Value("${github.token}") String githubToken,
            @Value("${sonar.sonarqube.url}") String sonarApiUrl,
            ProjectService projectService,
            UserService userService,
            GithubService githubService, ProjectValidationService projectValidationService
    ) {
        this.commitService = commitService;
        this.projectService = projectService;
        this.githubApiClient = new GithubApiClient(githubToken);
        this.sonarApiClient = new SonarApiClient(sonarApiUrl);
        this.userService = userService;
        this.githubService = githubService;
        this.projectValidationService = projectValidationService;
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

            System.out.println("SonarQube URL: " + SONARQUBE_URL);

            sonarAnalysis = new SonarAnalysis(project, commitSHA, SONARQUBE_URL);

            commitService.populateCommit(commit, project);
            project.addCommit(commit);
        }
    }

    public Project analyzeProject(String url, User user) throws Exception {
        log.info("AnalysisService - analyzeProject - url: " + url + " - user: " + user.getEmail());

        // Validate the URL
        projectValidationService.validateGithubUrl(url);

        // Get the organization of the user and create the project
        Organization organization = user.getOrganization();
        Project project = projectService.getOrCreateProject(url, organization);

        // Check if the Project meets the status requirements for analysis
        projectValidationService.validateProjectForAnalysis(project);

        // Call the Github API to fetch the project data
        githubService.fetchGithubData(project);

        // Make sure the project has less commits than the threshold
        projectValidationService.validateCommitThreshold(project);

        // Begin the analysis
        Integer analyzedCommits = startAnalysis(project);

        if (analyzedCommits == COMMITS_ANALYZED_ALREADY_FLAG) {
            throw new AnalysisException(HttpStatus.OK, "All commits are already analyzed, you can see the results on the dashboard.");
        }

        log.info("AnalysisService - analyzeProject - Analysis Finished! " + analyzedCommits + " commits analyzed");

        if (EXTERNAL_ANALYSIS_IS_ACTIVATED) externalAnalysisService.analyzeWithExternalServices(project);

        organizationAnalysisService.updateOrganizationAnalysis(organization);
        organizationService.saveOrganization(organization);
        log.info("AnalysisService - analyzeProject - Organization Analysis Updated");

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

                log.info("AnalysisService - analyzeProject - All commits are already analyzed, you can see the results on the dashboard");
                return COMMITS_ANALYZED_ALREADY_FLAG;
            }

            project.setStatus(ProjectStatus.ANALYSIS_STARTED);
            projectService.saveProject(project);

//            String defaultBranch = project.getDefaultBranchName();
//            RefactoringMinerAnalysis refactoringMinerAnalysis = new RefactoringMinerAnalysis(project.getRepoUrl(), defaultBranch, project.getName());
//            project.setTotalRefactorings(refactoringMinerAnalysis.getTotalNumberOfRefactorings());

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
