package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.exceptions.AnalysisException;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.external.CodeInspectorProjectStats;
import gr.uom.strategicplanning.models.stats.CodeInspectorStats;
import gr.uom.strategicplanning.repositories.CodeInspectorProjectStatsRepository;
import gr.uom.strategicplanning.repositories.CodeInspectorStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CodeInspectorService {
    private CodeInspectorStatsService codeInspectorStatsService;
    private CodeInspectorServiceStrategy codeInspectorServiceStrategy;

    @Value("${services.external.codeInspector.url}")
    private String CODE_INSPECTOR_URL = "http://localhost:8092";
    private String CODE_INSPECTOR_ANALYZE_HOTSPOTS = "/api/analysis/prioritize_hotspots";

    @Autowired
    public CodeInspectorService(
            CodeInspectorStatsService codeInspectorStatsService,
            RestTemplate restTemplate
    ) {
        this.codeInspectorStatsService = codeInspectorStatsService;
        this.codeInspectorServiceStrategy = new CodeInspectorServiceStrategy(restTemplate);
    }

    public Map<String, Object> analyzeHotspots(Project project) {
        log.info("Beggining analysis with CodeInspector");

        Map<String, String> dateRange = calculateAnalysisDateRange(project);
        Map<String, Object> params = Map.of(
                "endpointUrl", CODE_INSPECTOR_URL + CODE_INSPECTOR_ANALYZE_HOTSPOTS,
                "repo_url", project.getRepoUrl(),
                "from_date", dateRange.get("from_date"),
                "to_date", dateRange.get("to_date"),
                "method", HttpMethod.GET
        );

        log.info("Sending request to CodeInspector");

        ResponseEntity response = codeInspectorServiceStrategy.sendRequest(params);

        log.info("Received response from CodeInspector");

        checkIfResponseIsOK(response);

        // Convert the response to a map and return it
        return (Map<String, Object>) response.getBody();
    }

    public void updateHotspotStats(Project project, Map data) {
        codeInspectorStatsService.updateHotspotAnalysisProjectStats(project, data);
    }

    public void updateOrganizationHotspotStats(Organization org, CodeInspectorProjectStats stats) {
        codeInspectorStatsService.updateOrganizationStats(org);
    }

    public Map<String, String> calculateAnalysisDateRange(Project project) {
        String fromDate = project.getCreatedAt().substring(0, 10);
        String toDate = java.time.LocalDate.now().toString();

        return Map.of("from_date", fromDate, "to_date", toDate);
    }

    private void checkIfResponseIsOK(ResponseEntity response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("CodeInspector analysis failed with status code: " + response.getStatusCode());

            throw new AnalysisException(
                    response.getStatusCode(),
                    "CodeInspector analysis failed with status code: " + response.getStatusCode()
            );
        }
    }
}
