package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.exceptions.AnalysisException;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.external.CodeInspectorProjectStats;
import gr.uom.strategicplanning.services.CommitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CodeInspectorService {
    private CodeInspectorStatsService codeInspectorStatsService;
    private CommitService commitService;
    private CodeInspectorServiceStrategy codeInspectorServiceStrategy;

    @Value("${services.external.codeInspector.url}")
    private String CODE_INSPECTOR_URL = "http://localhost:8092";
    private String CODE_INSPECTOR_ANALYZE_HOTSPOTS = "/api/analysis/prioritize_hotspots";
    private String CODE_INSPECTOR_ANALYZE_COMMIT_QUALITY = "/api/analysis/commits";

    @Autowired
    public CodeInspectorService(
            CodeInspectorStatsService codeInspectorStatsService,
            CommitService commitService,
            RestTemplate restTemplate
    ) {
        this.codeInspectorStatsService = codeInspectorStatsService;
        this.commitService = commitService;
        this.codeInspectorServiceStrategy = new CodeInspectorServiceStrategy(restTemplate);
    }

    private Map<String, Object> sendAnalysisRequest(Project project, String endpoint) {
        log.info("Beggining analysis with CodeInspector:" + endpoint);

        Map<String, String> dateRange = calculateAnalysisDateRange(project);
        Map<String, Object> params = Map.of(
                "endpointUrl", endpoint,
                "repo_url", project.getRepoUrl(),
                "from_date", dateRange.get("from_date"),
                "to_date", dateRange.get("to_date"),
                "method", HttpMethod.GET
        );

        log.info("Sending request to CodeInspector");
        ResponseEntity response = codeInspectorServiceStrategy.sendRequest(params);
        checkIfResponseIsOK(response);

        log.info("Received response from CodeInspector");

        return (Map<String, Object>) response.getBody();
    }

    public Map<String, Object> analyzeHotspots(Project project) {
        log.info("Beggining analysis with CodeInspector");
        String endpoint = CODE_INSPECTOR_URL + CODE_INSPECTOR_ANALYZE_HOTSPOTS;
        return sendAnalysisRequest(project, endpoint);
    }

    public Map<String, Object> analyzeCommitQuality(Project project) {
        log.info("Beggining Commit analysis with CodeInspector");
        String endpoint = CODE_INSPECTOR_URL + CODE_INSPECTOR_ANALYZE_COMMIT_QUALITY;
        return sendAnalysisRequest(project, endpoint);
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

    public void updateCommitStats(Map<String, Object> codeInspectorResponse) {
        Map<String, Object> commitData = (Map<String, Object>) codeInspectorResponse.get("commit_analysis");

        Collection<Map> commits = (Collection<Map>) commitData.get("commits");

        for (Map commit : commits) {
            String hash = (String) commit.get("hash");
            Optional<Commit> commitOptional = commitService.getCommitByHash(hash);

            if (commitOptional.isEmpty()) continue;

            Commit commitToUpdate = commitOptional.get();

            double dmmSize = (double) commit.get("dmm_unit_size");
            double dmmComplexity = (double) commit.get("dmm_unit_complexity");
            double dmmInterfacing = (double) commit.get("dmm_unit_interfacing");
            String maintainabilityRating = (String) commit.get("change_category");

            commitToUpdate.setDmmUnitSize(dmmSize);
            commitToUpdate.setDmmComplexity(dmmComplexity);
            commitToUpdate.setDmmInterfacing(dmmInterfacing);
            commitToUpdate.setMaintainabilityRating(maintainabilityRating);

            commitService.saveCommit(commitToUpdate);
        }
    }
}
