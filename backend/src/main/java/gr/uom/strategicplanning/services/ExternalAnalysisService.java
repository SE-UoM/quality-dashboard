package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.strategies.PyAssessServiceStrategy;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.exceptions.ExternalAnalysisException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalAnalysisService {
    private final PyAssessServiceStrategy pyAssessServiceStrategy;
    private final CodeInspectorServiceStrategy codeInspectorServiceStrategy;

    @Value("${services.external.pyassess.url}")
    private String PYASSESS_URL;
    @Value("${services.external.codeInspector.url}")
    private String CODE_INSPECTOR_URL;

    private String CODE_INSPECTOR_ANALYZE_HOTSPOTS = "/api/analysis/prioritize_hotspots";
    private String PYASSESS_ANALYZE_REPO = "/api/analysis";

    public ExternalAnalysisService(RestTemplate restTemplate) {
        this.pyAssessServiceStrategy = new PyAssessServiceStrategy(restTemplate);
        this.codeInspectorServiceStrategy = new CodeInspectorServiceStrategy(restTemplate);
    }


    // Simulate analysis (just to make sure language finding works)
    private void fakeAnalysis(String name, int waitTime) {
        // Simulate analysis with CodeInspector (wait and print "waiting for CodeInspector")
        try {
            Thread.sleep(waitTime);
            System.out.println("--------- Simulated Analysis with " + name + " ---------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean analyzeWithExternalServices(Project project) {
        try {
            // Analysis with CodeInspector
            Map<String, String> params = new HashMap<>();
            params.put("endpointUrl", CODE_INSPECTOR_URL + CODE_INSPECTOR_ANALYZE_HOTSPOTS);
            params.put("gitUrl", project.getRepoUrl());

            codeInspectorServiceStrategy.sendRequest(params);
            project.setCodeInspectorStatus(ProjectStatus.ANALYSIS_COMPLETED);

            if (!project.hasLanguage("Python")) return false;

            // We won't get to this point if the project doesn't have Python as a language

            // Clear params for next request
            params.clear();

            // Analysis with PyAssess
            params.put("endpointUrl", PYASSESS_URL + PYASSESS_ANALYZE_REPO);
            params.put("gitUrl", project.getRepoUrl());
            params.put("token", null);
            params.put("ciToken", null);
            pyAssessServiceStrategy.sendRequest(params);
            // fakeAnalysis("PyAssess", 1000);
        }
        catch (Exception e) {
            if (e instanceof ExternalAnalysisException) {
                ExternalAnalysisException casted = (ExternalAnalysisException) e;

                String serviceName = casted.getExternalServiceName();

                if (serviceName.equalsIgnoreCase("CodeInspector"))
                    project.setCodeInspectorStatus(ProjectStatus.ANALYSIS_FAILED);

                if (serviceName.equalsIgnoreCase("PyAssess"))
                    project.setPyAssessStatus(ProjectStatus.ANALYSIS_FAILED);
            }
        }
        return true;
    }

}
