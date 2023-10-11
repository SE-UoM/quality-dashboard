package gr.uom.strategicplanning.analysis.external;

import gr.uom.strategicplanning.analysis.external.implementations.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.implementations.PyAssessServiceStrategy;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.SecondaryTable;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalServiceClient {

    private final RestTemplate restTemplate;
    private final PyAssessServiceStrategy pyAssessServiceStrategy;
    private final CodeInspectorServiceStrategy codeInspectorServiceStrategy;
    private final String PYASSESS_URL = "http://localhost:5000/api/analysis";
    private final String CODE_INSPECTOR_URL = "http://localhost:8000/api/analysis/prioritize_hotspots";



    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.pyAssessServiceStrategy = new PyAssessServiceStrategy(restTemplate);
        this.codeInspectorServiceStrategy = new CodeInspectorServiceStrategy(restTemplate);
    }

    public void analyzeWithExternalServices(Project project) {
        if(project.hasLanguage("Python")) {
            Map<String, String> params = new HashMap<>();
            params.put("endpointUrl", PYASSESS_URL);
            params.put("gitUrl", project.getRepoUrl());
            params.put("token", null);
            params.put("ciToken", null);

            pyAssessServiceStrategy.sendRequest(params);
        }

        Map<String, String> params = new HashMap<>();
        params.put("endpointUrl", CODE_INSPECTOR_URL);
        params.put("gitUrl", project.getRepoUrl());

        codeInspectorServiceStrategy.sendRequest(params);
    }

}
