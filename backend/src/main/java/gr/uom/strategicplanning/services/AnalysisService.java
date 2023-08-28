package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalyzer;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service
public class AnalysisService {
    private SonarAnalyzer sonarAnalyzer;
    private final GithubApiClient githubApiClient;
    private final CommitService commitService;
    private final ProjectService projectService;
    @Autowired
    private LanguageService languageService;

    @Autowired
    public AnalysisService(CommitService commitService, @Value("${github.token}") String githubToken, ProjectService projectService) {
        this.commitService = commitService;
        this.projectService = projectService;
        this.githubApiClient = new GithubApiClient(githubToken);
    }
    
    public void fetchGithubData(Project project) throws Exception {
        githubApiClient.fetchProjectData(project);
        project.setLanguages(languageService.extractLanguages(project));
    }

    public void startAnalysis(Project project) throws Exception {
        GithubApiClient.cloneRepository(project);

        List<String> commitList = githubApiClient.fetchCommitSHA(project);

        for (String commitSHA : commitList) {
            githubApiClient.checkoutCommit(project, commitSHA);
            Commit commit = new Commit();
            commit.setHash(commitSHA);

            sonarAnalyzer = new SonarAnalyzer(commitSHA);

            sonarAnalyzer.analyzeProject(project, commit);
            commitService.populateCommit(commit, project);
            project.addCommit(commit);
        }

        projectService.populateProject(project);
        GithubApiClient.deleteRepository(project);
    }


}
