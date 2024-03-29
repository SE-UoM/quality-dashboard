package gr.uom.strategicplanning.analysis.sonarqube;

import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.LogOutput;
import org.sonarsource.scanner.api.StdOutLogOutput;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for scanning projects with SonarQube.
 */
public class SonarScanner {
    private EmbeddedScanner scanner;
    private LogOutput logOutput;
    private Map<String, String> sonarProperties;

    @Value("${sonar.sonarqube.url}")
    private String sonarqubeUrl;

    /**
     * Constructs a new SonarScanner instance with the provided name and version.
     *
     * @param name    The name of the project.
     * @param version The version of the project.
     */
    public SonarScanner(String name, String version) {
        this.logOutput = new StdOutLogOutput();
        this.scanner = EmbeddedScanner.create(name, version, this.logOutput);
        this.sonarProperties = new HashMap<>();
        initProps();
    }

    /**
     * Initializes default SonarQube properties.
     * The default properties are:
     * - sonar.host.url: http://localhost:9000
     * - sonar.login: admin
     * - sonar.password: admin1
     */
    private void initProps() {
        this.sonarProperties.put("sonar.host.url", sonarqubeUrl);
//        this.sonarProperties.put("sonar.login", SonarApiClient.LOGIN_USERNAME);
//        this.sonarProperties.put("sonar.password", SonarApiClient.LOGIN_PASSWORD);
        this.sonarProperties.put("sonar.scm.disabled", "true");
    }

    /**
     * Adds a custom property to be used in the SonarQube analysis.
     *
     * @param key   The key of the property.
     * @param value The value of the property.
     * Example: addProperty("sonar.projectKey", "test");
     */
    public void addProperty(String key, String value) {
        this.sonarProperties.put(key, value);
    }

    /**
     * Starts the SonarQube scanner and performs the analysis using the configured properties.
     */
    public void start() {
        this.scanner.addGlobalProperties(this.sonarProperties);
        this.scanner.start();
    }

    /**
     * Executes the SonarQube scanner to analyze the project with the configured properties.
     */
    public void execute() {
        try {
            this.scanner.execute(this.sonarProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
