package gr.uom.strategicplanning.analysis.sonarqube;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.Project;

/**
 * A utility class for analyzing projects with SonarQube.
 */
public class SonarAnalyzer {
    private SonarScanner sonarScanner;
    SonarApiClient sonarApiClient = new SonarApiClient();
    
    /**
     * Constructs a new SonarAnalyzer instance.
     */
    public SonarAnalyzer(String name) {
        this.sonarScanner = new SonarScanner(name, "1.0");
    }

    /**
     * Analyzes the given project using SonarQube.
     * This method clones the project's repository, configures SonarQube properties,
     * runs the analysis, and cleans up the repository after the analysis is completed.
     *
     * @param project The Project object representing the project to be analyzed.
     * @param commit
     * @throws Exception If any error occurs during the analysis process.
     */
    public void analyzeProject(Project project) throws Exception {
        project.setStatus(ProjectStatus.ANALYSIS_STARTED);

        buildProps(project);

        sonarScanner.start();
        project.setStatus(ProjectStatus.ANALYSIS_IN_PROGRESS);

        sonarScanner.execute();

        project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);

        // Wait a bit to make sure the analysis data is available
        Thread.sleep(5000);
    }


    /**
     * Builds SonarQube properties based on the project information and adds them to the scanner.
     *
     * @param project The Project object representing the project to be analyzed.
     */
    private void buildProps(Project project) {
       sonarScanner.addProperty("sonar.projectKey", project.getName());
       sonarScanner.addProperty("sonar.sources", "./repos/" + project.getName());
       sonarScanner.addProperty("sonar.java.binaries", "./repos/" + project.getName());
       sonarScanner.addProperty("sonar.cxx.file.suffixes", ".cc,.cpp,.cxx,.c++,.hh,.hpp,.hxx,.h++,.c,.h");
    }
}
