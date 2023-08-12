package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalyzer;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AnalysisService {
    private SonarAnalyzer sonarAnalyzer;
    private final GithubApiClient githubApiClient = new GithubApiClient();
    private final CommitService commitService;
    private final ProjectService projectService = new ProjectService();
    @Autowired
    private LanguageService languageService;

    @Autowired
    public AnalysisService(CommitService commitService) {
        this.commitService = commitService;
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
