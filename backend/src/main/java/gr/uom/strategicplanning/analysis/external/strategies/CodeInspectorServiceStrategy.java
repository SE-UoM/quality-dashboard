package gr.uom.strategicplanning.analysis.external.strategies;

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
    public String constructUrl(Map<String, String> params) {
        String endpointUrl = params.get("endpointUrl");
        String gitUrl = params.get("gitUrl");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpointUrl);
        builder.queryParam("repo_url", gitUrl);

        String analysisEndpointUrl = builder.toUriString();

        return analysisEndpointUrl;
    }

    /**
     * Sends a request to the Code Inspector service using the provided parameters.
     *
     * @param params A map of parameters used for the request.
     */
    @Override
    public void sendRequest(Map<String, String> params) {
        String analysisEndpointUrl = constructUrl(params);
        HttpHeaders headers = createJsonHttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = super.getRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                analysisEndpointUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        boolean responseFailed = response.getStatusCode() != HttpStatus.OK;
        if (responseFailed) throw new RuntimeException("Error when sending request to CodeInspector service");
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
