package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalysis;
import gr.uom.strategicplanning.analysis.refactoringminer.RefactoringMinerAnalysis;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.ProjectStats;
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
public class AnalysisService {
    private final SonarApiClient sonarApiClient;
    private final GithubApiClient githubApiClient;
    private final CommitService commitService;
    private final ProjectService projectService;
    private SonarAnalysis sonarAnalysis;

    @Value("${sonar.sonarqube.url}")
    private String SONARQUBE_URL;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private ProjectStatsService projectStatsService;

    @Autowired
    private CodeSmellDistributionService codeSmellDistributionService;

    @Autowired
    public AnalysisService(
            CommitService commitService,
            @Value("${github.token}") String githubToken,
            @Value("${sonar.sonarqube.url}") String sonarApiUrl,
            ProjectService projectService) {
        this.commitService = commitService;
        this.projectService = projectService;
        this.githubApiClient = new GithubApiClient(githubToken);
        this.sonarApiClient = new SonarApiClient(sonarApiUrl);
    }

    public void fetchGithubData(Project project) throws Exception {
        githubApiClient.fetchProjectData(project);
    }

    public boolean validateUrlWithGithub(String url) {
        return githubApiClient.repoFoundByGithubAPI(url);
    }

    public boolean repoHasLessThatThresholdCommits(Project project) {
        return githubApiClient.repoHasLessThatThresholdCommits(project, OrganizationAnalysis.COMMITS_THRESHOLD);
    }

    private void analyzeCommits(Project project) throws Exception {
        List<String> commitList = githubApiClient.fetchCommitSHA(project);
        List<String> commitListFinal = new ArrayList<>();
        for (String sha: commitList){
            if(project.getCommits().stream().anyMatch(x-> Objects.equals(x.getHash(), sha)))
                continue;
            commitListFinal.add(sha);
        }
        Collections.reverse(commitListFinal);

        for (String commitSHA : commitListFinal) {
            System.out.println("Analyzing " + commitListFinal.indexOf(commitSHA)+1 + " out of " + commitListFinal.size() + " commits");
            githubApiClient.checkoutCommit(project, commitSHA);

            Commit commit = new Commit();
            commit.setHash(commitSHA);

            sonarAnalysis = new SonarAnalysis(project, commitSHA, SONARQUBE_URL);

            commitService.populateCommit(commit, project);
            project.addCommit(commit);
        }
    }

    private void analyzeMaster(Project project) throws Exception {
        githubApiClient.checkoutMaster(project);

        sonarAnalysis = new SonarAnalysis(project, project.getDefaultBranchName(), SONARQUBE_URL);
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

    public void startAnalysis(Project project) throws Exception {
        Git clonedGit = GithubApiClient.cloneRepository(project);

        try {
            String sha = githubApiClient.getShaOfClonedProject(clonedGit);

            if(project.getCommits().stream().anyMatch(x-> Objects.equals(x.getHash(), sha))){
                clonedGit.close();
                GithubApiClient.deleteRepository(project);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "The Repo's last version has been analyzed!"
                );
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
            GithubApiClient.deleteRepository(project);
        } catch (Exception e) {
            clonedGit.close();
            GithubApiClient.deleteRepository(project);

            project.setStatus(ProjectStatus.ANALYSIS_FAILED);
            projectService.saveProject(project);
        }
    }

}
