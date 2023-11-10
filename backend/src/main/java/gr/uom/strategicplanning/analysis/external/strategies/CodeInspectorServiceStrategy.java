package gr.uom.strategicplanning.analysis.external.strategies;

import gr.uom.strategicplanning.models.exceptions.ExternalAnalysisException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * This class represents a strategy for interacting with the Code Inspector service.
 */
public class CodeInspectorServiceStrategy extends ExternalServiceStrategyImplementation{
    public CodeInspectorServiceStrategy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Constructs the URL for the Code Inspector service based on the provided parameters.
     *
     * @param params A map of parameters ("endpointUrl", "repoUrl").
     * @return The constructed URL as a string.
     */

    @Override
    public String constructUrl(Map<String, Object> params) {
        String endpointUrl = (String) params.get("endpointUrl");
        String gitUrl = (String) params.get("gitUrl");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpointUrl);

        if (gitUrl != null) builder.queryParam("repo_url", gitUrl);

        return builder.toUriString();
    }

    /**
     * Sends a request to the Code Inspector service using the provided parameters.
     * @param params A map of parameters used for the request.
     */
    @Override
    public ResponseEntity sendRequest(Map<String, Object> params) {
        HttpMethod method = (HttpMethod) params.get("method");

        String analysisEndpointUrl = constructUrl(params);
        HttpHeaders headers = createJsonHttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = super.getRestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                analysisEndpointUrl,
                method,
                requestEntity,
                Map.class
        );

        boolean responseFailed = response.getStatusCode() != HttpStatus.OK;
        if (responseFailed) throw new ExternalAnalysisException("CODEINSPECTOR");

        return response;
    }

    public static void main(String[] args) {
        Map params = Map.of(
                "endpointUrl", "http://localhost:8000/api/analysis/prioritize_hotspots",
                "repoUrl", "https://github.com/GeorgeApos/pyassess"
        );

        CodeInspectorServiceStrategy caller = new CodeInspectorServiceStrategy(new RestTemplate());
        caller.sendRequest(params);
    }
}
