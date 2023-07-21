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
        String[] split = project.getRepoUrl().split("/");
        String owner = split[split.length - 2];
        String name = split[split.length - 1];

        cloneRepository(project.getRepoUrl());
        sonarAnalyzer.beginAnalysis(project, owner, name);
        deleteRepository(project.getRepoUrl());
    }

    public void cloneRepository(String url) throws Exception {
        Git.cloneRepository()
                .setURI(url)
                .setDirectory(new File(System.getProperty("user" + ".dir") + "/repos" + "/" + extractRepoName(url)))
                .call();
    }

    public void deleteRepository(String url) throws Exception {
        String repoName = extractRepoName(url);
        File file = new File(System.getProperty("user" + ".dir") + "/repos/" + repoName);
        file.delete();
    }

    private String extractRepoName(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }

}
