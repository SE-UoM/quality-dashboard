package gr.uom.strategicplanning.analysis.external.strategies;

import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.exceptions.ExternalAnalysisException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a strategy for interacting with the PyAssess service.
 */

@Service
@Getter @Setter
public class PyAssessServiceStrategy extends ExternalServiceStrategyImplementation {
    public PyAssessServiceStrategy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Constructs the URL for the PyAssess service based on the provided parameters.
     *
     * @param params A map of parameters ("endpointUrl," "gitUrl," "token," and "ciToken").
     * @return The constructed URL as a string.
     */
    @Override
    public String constructUrl(Map<String, String> params) {
        String endpointUrl = params.get("endpointUrl");
        String gitUrl = params.get("gitUrl");
        String token = params.get("token");
        String ciToken = params.get("ciToken");

        // Construct the URL with query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpointUrl);
        builder.queryParam("gitUrl", gitUrl);
        if (token != null) {
            builder.queryParam("token", token);
        }
        if (ciToken != null) {
            builder.queryParam("ciToken", ciToken);
        }

        String analysisEndpointUrl = builder.toUriString();

        return analysisEndpointUrl;
    }

    /**
     * Sends a request to the PyAssess service using the provided parameters.
     *
     * @param params A map of parameters used for the request ("endpointUrl," "gitUrl," "token," and "ciToken").
     */
    @Override
    public void sendRequest(Map<String, String> params) {
        String analysisEndpointUrl = constructUrl(params);
        HttpHeaders headers = createJsonHttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = super.getRestTemplate();
        ResponseEntity<Project> response = restTemplate.exchange(
                analysisEndpointUrl,
                HttpMethod.POST,
                requestEntity,
                Project.class
        );

        boolean responseFailed = response.getStatusCode() != HttpStatus.OK;

        if (responseFailed) throw new ExternalAnalysisException("PYASSESS");
    }
}
