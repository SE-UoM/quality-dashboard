package gr.uom.strategicplanning.analysis.external.implementations;

import gr.uom.strategicplanning.analysis.external.ExternalServiceStrategy;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * This abstract class provides a base implementation for the ExternalServiceStrategy interface.
 * It is not intended to be used directly but to be extended by concrete strategy implementations.
 */
@Getter @Setter
public abstract class ExternalServiceStrategyImplementation implements ExternalServiceStrategy {
    private final RestTemplate restTemplate;

    public ExternalServiceStrategyImplementation(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected HttpHeaders createJsonHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public String constructUrl(Map<String, String> params) {
        throw new NotYetImplementedException("ExternalServiceStrategyImplementation.constructUrl() not implemented");
    }

    @Override
    public void sendRequest(Map<String, String> params) {
         throw new NotYetImplementedException("ExternalServiceStrategyImplementation.sendRequest() not implemented");
    }
}
