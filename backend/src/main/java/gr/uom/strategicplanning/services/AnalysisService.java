package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalyzer;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.stereotype.Service;


@Service
public class AnalysisService {
    private SonarAnalyzer sonarAnalyzer = new SonarAnalyzer();
    private GithubApiClient githubApiClient = new GithubApiClient();

    public void fetchGithubData(Project project) throws Exception {
        githubApiClient.fetchProjectData(project);
    }

    public void startAnalysis(Project project) throws Exception {
        sonarAnalyzer.analyzeProject(project);
    }
}
