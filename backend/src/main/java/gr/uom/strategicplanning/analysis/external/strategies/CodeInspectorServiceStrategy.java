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
        String gitUrl = (String) params.get("repo_url");
        String from = (String) params.get("from_date");
        String to = (String) params.get("to_date");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpointUrl);

        if (gitUrl != null) builder.queryParam("repo_url", gitUrl);
        if (from != null) builder.queryParam("from_date", from);
        if (to != null) builder.queryParam("to_date", to);

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

}
