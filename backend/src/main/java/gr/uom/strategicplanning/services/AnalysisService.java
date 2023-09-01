package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalyzer;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AnalysisService {
    private SonarAnalyzer sonarAnalyzer;
    private SonarApiClient sonarApiClient;
    private final GithubApiClient githubApiClient;
    private final CommitService commitService;
    private final ProjectService projectService;
    @Autowired
    private LanguageService languageService;

    @Autowired
    private ProjectStatsService projectStatsService;

    @Autowired
    private CodeSmellDistributionService codeSmellDistributionService;

    @Autowired
    public AnalysisService(CommitService commitService, @Value("${github.token}") String githubToken, ProjectService projectService) {
        this.commitService = commitService;
        this.projectService = projectService;
        this.githubApiClient = new GithubApiClient(githubToken);
        this.sonarApiClient = new SonarApiClient();
    }
    
    public void fetchGithubData(Project project) throws Exception {
        githubApiClient.fetchProjectData(project);

    }

    public void startAnalysis(Project project) throws Exception {
        GithubApiClient.cloneRepository(project);

        project.getCommits().clear();

        List<String> commitList = githubApiClient.fetchCommitSHA(project);

        for (String commitSHA : commitList) {
            githubApiClient.checkoutCommit(project, commitSHA);
            Commit commit = new Commit();
            commit.setHash(commitSHA);

            sonarAnalyzer = new SonarAnalyzer(commitSHA);

            sonarAnalyzer.analyzeProject(project);
            commitService.populateCommit(commit, project);
            project.addCommit(commit);
        }

//         Analyze the master branch to get the latest analysis of the project
        githubApiClient.checkoutMasterWithLatestCommit(project);
        sonarAnalyzer = new SonarAnalyzer("master");
        sonarAnalyzer.analyzeProject(project);

        // Wait a bit to make sure the analysis data is available
        Thread.sleep(5000);


        Collection languages = languageService.extractLanguagesFromProject(project);

        int totalLanguages = languages.size();
        int totalDevelopers = project.getDevelopers().size();
        int totalCommits = project.getCommits().size();

        Collection<CodeSmellDistribution> codeSmellsDistribution = sonarApiClient.fetchCodeSmellsDistribution(project);
        codeSmellDistributionService.saveCollectionOfCodeSmellDistribution(codeSmellsDistribution);

        ProjectStats projectStats = project.getProjectStats();
        projectStats.setCodeSmellDistributions(codeSmellsDistribution);
        projectStatsService.saveProjectStats(projectStats);

        project.setTotalCommits(totalCommits);
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

        projectService.saveProject(project);
        GithubApiClient.deleteRepository(project);
    }

}
