package gr.uom.strategicplanning.analysis.sonarqube;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.Project;

import java.io.File;

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

        GithubApiClient.cloneRepository(project);

        buildProps(project);

        sonarScanner.start();
        project.setStatus(ProjectStatus.ANALYSIS_IN_PROGRESS);

        sonarScanner.execute();

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

       for (Language language : project.getLanguages()) {
           if (language.is("Java")) {
               sonarScanner.addProperty("sonar.java.binaries", "./repos/" + project.getName());
           } else if (language.is("C") || language.is("C++")) {
               sonarScanner.addProperty("sonar.cxx.file.suffixes", ".cc,.cpp,.cxx,.c++,.hh,.hpp,.hxx,.h++,.c,.h");
           }
       }
    }
}
