package gr.uom.strategicplanning.analysis.sonarqube;

import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.LogOutput;
import org.sonarsource.scanner.api.StdOutLogOutput;

import java.util.HashMap;
import java.util.Map;

public class SonarScanner {
    private EmbeddedScanner scanner;
    private LogOutput logOutput;
    private Map<String, String> sonarProperties;

    public SonarScanner(String name, String version) {
        this.logOutput = new StdOutLogOutput();
        this.scanner = EmbeddedScanner.create(name, version, this.logOutput);
        this.sonarProperties = new HashMap<>();
        initProps();
    }

    private void initProps() {
        this.sonarProperties.put("sonar.host.url", "http://localhost:9000");
        this.sonarProperties.put("sonar.login", "admin");
        this.sonarProperties.put("sonar.password", "admin1");
        this.sonarProperties.put("sonar.scm.disabled", "true");
    }

    public void addProperty(String key, String value) {
        this.sonarProperties.put(key, value);
    }

    public void start() {
        this.scanner.addGlobalProperties(this.sonarProperties);
        this.scanner.start();
    }

    public void execute() {
        this.scanner.execute(this.sonarProperties);
    }
}
