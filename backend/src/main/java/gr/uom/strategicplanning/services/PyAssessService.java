package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.strategies.PyAssessServiceStrategy;
import gr.uom.strategicplanning.exceptions.AnalysisException;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.external.PyAssessProjectStats;
import gr.uom.strategicplanning.models.stats.PyAssessStats;
import gr.uom.strategicplanning.repositories.PyAssessProjectStatsRepository;
import gr.uom.strategicplanning.repositories.PyAssessStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PyAssessService {
    private PyAssessStatsService pyAssessStatsService;

    @Value("${services.external.pyassess.url}")
    private String PYASSESS_URL;

    private String PYASSESS_ANALYZE_REPO = "/project_analysis";

    @Value("${github.token}")
    private String GIT_TOKEN;

    private final PyAssessServiceStrategy pyAssessServiceStrategy;

    public static final int ALREADY_ANALYZED_FLAG = -2;
    public static final int NOT_PYTHON_PROJECT_FLAG = -1;
    public static final int ANALYSIS_COMPLETED_FLAG = 1;

    @Autowired
    public PyAssessService(
            PyAssessStatsService pyAssessStatsService,
            RestTemplate restTemplate
    ) {
        this.pyAssessServiceStrategy = new PyAssessServiceStrategy(restTemplate);
        this.pyAssessStatsService = pyAssessStatsService;
    }

    public int sendAnalysisRequest(Project project) {
        // If the project does not have PYTHON as a language, do not send the request
        if (!project.hasLanguage("Python")) return NOT_PYTHON_PROJECT_FLAG;

        // Prepare the request
        Map<String, Object> startAnalysisRParams = Map.of(
                "endpointUrl", PYASSESS_URL + PYASSESS_ANALYZE_REPO,
                "gitUrl", project.getRepoUrl(),
                "token", GIT_TOKEN,
                "method", "GET"
        );

        // Send the request
        log.info("Sending request to PyAssess");
        ResponseEntity response = pyAssessServiceStrategy.sendRequest(startAnalysisRParams);

        if (response.getStatusCode() == HttpStatus.CONFLICT) return ALREADY_ANALYZED_FLAG;
        checkIfResponseIsOK(response);

        // If we get here it means the response is OK and the analysis was successful
        return ANALYSIS_COMPLETED_FLAG;
    }

    public Optional<Map<String, Object>> getAnalysisResults(Project project) {
        // Prepare the request
        Map<String, Object> getAnalysisResultsParams = Map.of(
                "endpointUrl", PYASSESS_URL + PYASSESS_ANALYZE_REPO,
                "gitUrl", project.getRepoUrl(),
                "branch", project.getDefaultBranchName(),
                "token", GIT_TOKEN,
                "method", "GET"
        );

        // Send the request
        log.info("Sending request to PyAssess");
        ResponseEntity response = pyAssessServiceStrategy.sendRequest(getAnalysisResultsParams);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) return Optional.empty();
        checkIfResponseIsOK(response);

        // Convert the response to a map and return it
        return Optional.of((Map<String, Object>) response.getBody());
    }

    private void checkIfResponseIsOK(ResponseEntity response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("PyAssess analysis failed with status code: " + response.getStatusCode());

            throw new AnalysisException(
                    response.getStatusCode(),
                    "PyAssess analysis failed with status code: " + response.getStatusCode()
            );
        }
    }

    public void updateProjectStats(Project project, Map analysisItems) {
        this.pyAssessStatsService.updateProjectStats(project, analysisItems);
    }

    public void updateOrganizationStats(Organization org) {
        this.pyAssessStatsService.updateOrganizationStats(org);
    }
}
