package gr.uom.strategicplanning.analysis.external;

import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.SecondaryTable;

@Service
public class ExternalServiceClient {

    private final RestTemplate restTemplate;

    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendPostRequestToAnalysisEndpoint(
            String endpointUrl,
            String gitUrl,
            String token,
            String citoken
    ) {
        // Construct the URL with query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpointUrl);
        builder.queryParam("gitUrl", gitUrl);
        if (token != null) {
            builder.queryParam("token", token);
        }
        if (citoken != null) {
            builder.queryParam("citoken", citoken);
        }

        String analysisEndpointUrl = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Project> response = restTemplate.exchange(
                analysisEndpointUrl,
                HttpMethod.POST,
                requestEntity,
                Project.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Error while sending request to external service");
        }
    }

}
