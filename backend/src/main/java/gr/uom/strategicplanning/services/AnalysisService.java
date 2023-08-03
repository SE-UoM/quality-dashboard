package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalyzer;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AnalysisService {
    private SonarAnalyzer sonarAnalyzer = new SonarAnalyzer();
    private GithubApiClient githubApiClient = new GithubApiClient();
    private CommitService commitService = new CommitService();

    private OrganizationAnalysisService organizationAnalysisService = new OrganizationAnalysisService();

    public void fetchGithubData(Project project) throws Exception {
        githubApiClient.fetchProjectData(project);
    }

    public void startAnalysis(Project project) throws Exception {

        List<String> commitList = githubApiClient.fetchCommitSHA(project);

        for (String commitSHA : commitList) {
            githubApiClient.checkoutCommit(project, commitSHA);
            Commit commit = new Commit();
            commit.setHash(commitSHA);
            sonarAnalyzer.analyzeProject(project, commit);
            commitService.populateCommit(commit, project);
            project.addCommit(commit);
        }

        GithubApiClient.deleteRepository(project);

    }
}
