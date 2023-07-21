package gr.uom.strategicplanning.analysis.sonarqube;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.Project;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.StdOutLogOutput;

import java.util.HashMap;
import java.util.Map;

public class SonarAnalyzer {

    private EmbeddedScanner sonarScanner;

    private Map<String,String> props;

    public void beginAnalysis(Project project, String owner, String name) {
        project.setStatus(ProjectStatus.ANALYSIS_STARTED);
        buildProps(project);
        initSonarAnalysis(project);
        project.setStatus(ProjectStatus.ANALYSIS_IN_PROGRESS);
        executeAnalysis(project, props);
        project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
    }

    private void buildProps(Project project){
        this.props = new HashMap<>();
        props.put("sonar.projectKey", project.getName());
        props.put("sonar.sources", "./repos/" + project.getName());
        props.put("sonar.host.url", "http://localhost:9000");
        props.put("sonar.login", "admin");
        props.put("sonar.password", "admin");
    }

    private void initSonarAnalysis(Project project){
        sonarScanner = EmbeddedScanner.create("dashboard-scanner", "1.0", new StdOutLogOutput());
        sonarScanner.addGlobalProperties(props);
        sonarScanner.start();
    }

    private void executeAnalysis(Project project, Map<String,String> props){
        sonarScanner.execute(props);
    }

}
