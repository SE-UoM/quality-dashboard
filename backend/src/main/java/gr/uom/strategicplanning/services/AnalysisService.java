package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarAnalyzer;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.Project;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.lib.Ref;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.eclipse.jgit.api.Git;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Collections;
import java.util.Map;


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
