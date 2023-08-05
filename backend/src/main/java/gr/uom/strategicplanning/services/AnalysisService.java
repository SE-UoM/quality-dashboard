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
    private final SonarAnalyzer sonarAnalyzer = new SonarAnalyzer();
    private final GithubApiClient githubApiClient = new GithubApiClient();
    private final CommitService commitService;
    private final ProjectService projectService = new ProjectService();

    @Autowired
    public AnalysisService(CommitService commitService) {
        this.commitService = commitService;
    }
    
    public void fetchGithubData(Project project) throws Exception {
        githubApiClient.fetchProjectData(project);
    }

    public void startAnalysis(Project project) throws Exception {
        GithubApiClient.cloneRepository(project);
        List<String> commitList = githubApiClient.fetchCommitSHA(project);

        for (String commitSHA : commitList) {
            githubApiClient.checkoutCommit(project, commitSHA);
            Commit commit = new Commit();
            commit.setHash(commitSHA);
            sonarAnalyzer.analyzeProject(project, commit);
            commitService.populateCommit(commit, project);
            project.addCommit(commit);
        }

        projectService.populateProject(project);
        projectService.saveProject(project);

        GithubApiClient.deleteRepository(project);

    }
}
