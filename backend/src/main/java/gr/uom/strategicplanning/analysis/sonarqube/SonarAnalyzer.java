package gr.uom.strategicplanning.analysis.sonarqube;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.StdOutLogOutput;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SonarAnalyzer {
    private SonarScanner sonarScanner;

    public SonarAnalyzer() {
        this.sonarScanner = new SonarScanner("dashboard-scanner", "1.0");
    }

    public void analyzeProject(Project project) throws Exception {
        project.setStatus(ProjectStatus.ANALYSIS_STARTED);

        GithubApiClient.cloneRepository(project);

        buildProps(project);

        sonarScanner.start();
        project.setStatus(ProjectStatus.ANALYSIS_IN_PROGRESS);
        sonarScanner.execute();

        project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
        GithubApiClient.deleteRepository(project);
    }

    private void buildProps(Project project){
        sonarScanner.addProperty("sonar.projectKey", project.getName());
        sonarScanner.addProperty("sonar.sources", "./repos/" + project.getName());
    }

    public static void main(String[] args) throws Exception {
        SonarAnalyzer sonarAnalyzer = new SonarAnalyzer();
        Project project = new Project();
        project.setName("test");
        project.setRepoUrl("https://github.com/RayVentura/ShortGPT");

        sonarAnalyzer.analyzeProject(project);
    }
}
