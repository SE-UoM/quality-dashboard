package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.strategies.PyAssessServiceStrategy;
import gr.uom.strategicplanning.models.domain.Project;
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

    public ExternalAnalysisService(RestTemplate restTemplate) {
        this.pyAssessServiceStrategy = new PyAssessServiceStrategy(restTemplate);
        this.codeInspectorServiceStrategy = new CodeInspectorServiceStrategy(restTemplate);
    }

    public boolean analyzeWithExternalServices(Project project) {
        // Analysis with CodeInspector
        Map<String, String> params = new HashMap<>();
        params.put("endpointUrl", CODE_INSPECTOR_URL);
        params.put("gitUrl", project.getRepoUrl());

        codeInspectorServiceStrategy.sendRequest(params);

        if (!project.hasLanguage("Python")) return false;

        // We won't get to this point if the project doesn't have Python as a language

        // Clear params for next request
        params.clear();

        // Analysis with PyAssess
        params.put("endpointUrl", PYASSESS_URL);
        params.put("gitUrl", project.getRepoUrl());
        params.put("token", null);
        params.put("ciToken", null);
        pyAssessServiceStrategy.sendRequest(params);

        return true;
    }

}
