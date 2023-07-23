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

/**
 * A utility class for analyzing projects with SonarQube.
 */
public class SonarAnalyzer {
    private SonarScanner sonarScanner;

    /**
     * Constructs a new SonarAnalyzer instance.
     */
    public SonarAnalyzer() {
        this.sonarScanner = new SonarScanner("dashboard-scanner", "1.0");
    }

    /**
     * Analyzes the given project using SonarQube.
     * This method clones the project's repository, configures SonarQube properties,
     * runs the analysis, and cleans up the repository after the analysis is completed.
     *
     * @param project The Project object representing the project to be analyzed.
     * @throws Exception If any error occurs during the analysis process.
     */
    public void analyzeProject(Project project) throws Exception {
        project.setStatus(ProjectStatus.ANALYSIS_STARTED);

        // Clone the project's repository using the GithubApiClient.
        GithubApiClient.cloneRepository(project);

        // Build SonarQube properties based on the project information.
        buildProps(project);

        // Start SonarQube scanner and set analysis status to in-progress.
        sonarScanner.start();
        project.setStatus(ProjectStatus.ANALYSIS_IN_PROGRESS);

        // Execute the SonarQube analysis.
        sonarScanner.execute();

        // Set the analysis status to completed and delete the cloned repository.
        project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
        GithubApiClient.deleteRepository(project);
    }

    /**
     * Builds SonarQube properties based on the project information and adds them to the scanner.
     *
     * @param project The Project object representing the project to be analyzed.
     */
    private void buildProps(Project project) {
        sonarScanner.addProperty("sonar.projectKey", project.getName());
        sonarScanner.addProperty("sonar.sources", "./repos/" + project.getName());
    }

    /**
     * Main method for testing the SonarAnalyzer class.
     *
     * @param args Command-line arguments (not used in this method).
     * @throws Exception If any error occurs during the analysis process.
     */
    public static void main(String[] args) throws Exception {
        SonarAnalyzer sonarAnalyzer = new SonarAnalyzer();
        Project project = new Project();
        project.setName("test");
        project.setRepoUrl("https://github.com/RayVentura/ShortGPT");

        // Analyze the test project.
        sonarAnalyzer.analyzeProject(project);
    }
}
