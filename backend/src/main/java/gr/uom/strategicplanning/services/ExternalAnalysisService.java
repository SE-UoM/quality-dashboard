package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.strategies.PyAssessServiceStrategy;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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

    public ExternalAnalysisService(RestTemplate restTemplate) {
        this.pyAssessServiceStrategy = new PyAssessServiceStrategy(restTemplate);
        this.codeInspectorServiceStrategy = new CodeInspectorServiceStrategy(restTemplate);
    }

    @Async
    public void analyzeWithCodeInspector(Project project) {
        Map<String, String> params = new HashMap<>();
        params.put("endpointUrl", CODE_INSPECTOR_URL + "/api/analysis/prioritize_hotspots");
        params.put("gitUrl", project.getRepoUrl());
        codeInspectorServiceStrategy.sendRequest(params);
    }

    @Async
    public void analyzeWithPyAssess(Project project) {
        Map<String, String> params = new HashMap<>();
        params.put("endpointUrl", PYASSESS_URL + "/api/analysis");
        params.put("gitUrl", project.getRepoUrl());
        params.put("token", null);
        params.put("ciToken", null);
        pyAssessServiceStrategy.sendRequest(params);
    }

    public boolean analyzeWithExternalServices(Project project) {
        analyzeWithCodeInspector(project);

        if (!project.hasLanguage("Python")) return false;

        // We won't get to this point if the project doesn't have Python as a language

        analyzeWithPyAssess(project);

        return true;
    }
}
