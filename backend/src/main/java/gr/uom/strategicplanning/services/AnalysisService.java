package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.Project;
import org.springframework.stereotype.Service;

@Service
public class AnalysisService {

    private GithubApiClient githubApiClient = new GithubApiClient();

    public void fetchGithubData(Project project) throws Exception {
        githubApiClient.fetchProjectData(project);
    }

    public void startAnalysis(Project project) throws Exception {
        //TODO: Configure sonarqube analysis

    }

}
