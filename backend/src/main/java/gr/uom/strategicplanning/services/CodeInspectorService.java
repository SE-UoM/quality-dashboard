package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.external.CodeInspectorProjectStats;
import gr.uom.strategicplanning.models.stats.CodeInspectorStats;
import gr.uom.strategicplanning.repositories.CodeInspectorProjectStatsRepository;
import gr.uom.strategicplanning.repositories.CodeInspectorStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CodeInspectorService {

    @Autowired
    private CodeInspectorProjectStatsRepository codeInspectorProjectStatsRepository;
    @Autowired
    private CodeInspectorStatsRepository codeInspectorStatsRepository;

    public void populateCodeInspectorModel(ResponseEntity response) {

        Object body = response.getBody();
        if (body != null && body instanceof Map) {
            Map<String, Object> bodyMap = (Map<String, Object>) body;
            String gitUrl = (String) bodyMap.get("gitUrl");
            if (gitUrl != null) {

                Optional<CodeInspectorProjectStats> codeInspectorProjectStatsOptional = codeInspectorProjectStatsRepository.findByProjectUrl(gitUrl);
                CodeInspectorProjectStats codeInspectorProjectStats;
                codeInspectorProjectStats = codeInspectorProjectStatsOptional.orElseGet(CodeInspectorProjectStats::new);
                populateModels(bodyMap, codeInspectorProjectStats);
                populateOrganizationalStats(codeInspectorProjectStats);
            } else {
                // there was a problem with the response
            }
        } else {
            System.out.println("Response body is null or not a Map");
        }

    }

    private void populateModels(Map<String, Object> bodyMap, CodeInspectorProjectStats codeInspectorProjectStats) {
        codeInspectorProjectStats.setProjectName((String) bodyMap.get("project_name"));
        codeInspectorProjectStats.setProjectUrl((String) bodyMap.get("repo_url"));

        try {
            String fromDateStr = (String) bodyMap.get("from_date");
            String toDateStr = (String) bodyMap.get("to_date");
            codeInspectorProjectStats.setFromDate(java.sql.Timestamp.valueOf(fromDateStr.replace("T", " ").substring(0, 19)));
            codeInspectorProjectStats.setToDate(java.sql.Timestamp.valueOf(toDateStr.replace("T", " ").substring(0, 19)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        codeInspectorProjectStats.setAverageComplexity(((Number) bodyMap.get("avg_complexity")).longValue());
        codeInspectorProjectStats.setAverageChurn(((Number) bodyMap.get("avg_churn")).longValue());
        codeInspectorProjectStats.setAverageNloc(((Number) bodyMap.get("avg_nloc")).longValue());

        codeInspectorProjectStats.setTotalNloc(((Number) bodyMap.get("total_nloc")).intValue());
        codeInspectorProjectStats.setTotalFiles(((Number) bodyMap.get("total_files")).intValue());
        codeInspectorProjectStats.setTotalOutliers(((Number) bodyMap.get("total_outliers")).intValue());
        codeInspectorProjectStats.setTotalPrioritizedFiles(((Number) bodyMap.get("total_prioritized_files")).intValue());

        codeInspectorProjectStatsRepository.save(codeInspectorProjectStats);
    }


    private void populateOrganizationalStats(CodeInspectorProjectStats codeInspectorProjectStats) {
        // Fetch existing organizational stats based on project URL or create new
        Optional<CodeInspectorStats> codeInspectorStatsOptional = codeInspectorProjectStatsRepository
                .findStatsByProjectUrl(codeInspectorProjectStats.getProjectUrl());
        CodeInspectorStats codeInspectorStats = codeInspectorStatsOptional.orElseGet(CodeInspectorStats::new);

        if (codeInspectorStats.getAverageComplexity() != null) {
            codeInspectorStats.setAverageComplexity(
                    (codeInspectorStats.getAverageComplexity() + codeInspectorProjectStats.getAverageComplexity()) / 2
            );
        } else {
            codeInspectorStats.setAverageComplexity(codeInspectorProjectStats.getAverageComplexity());
        }

        if (codeInspectorStats.getAverageChurn() != null) {
            codeInspectorStats.setAverageChurn(
                    (codeInspectorStats.getAverageChurn() + codeInspectorProjectStats.getAverageChurn()) / 2
            );
        } else {
            codeInspectorStats.setAverageChurn(codeInspectorProjectStats.getAverageChurn());
        }

        if (codeInspectorStats.getAverageNloc() != null) {
            codeInspectorStats.setAverageNloc(
                    (codeInspectorStats.getAverageNloc() + codeInspectorProjectStats.getAverageNloc()) / 2
            );
        } else {
            codeInspectorStats.setAverageNloc(codeInspectorProjectStats.getAverageNloc());
        }

        codeInspectorStats.setTotalNloc(
                codeInspectorStats.getTotalNloc() + codeInspectorProjectStats.getTotalNloc()
        );

        codeInspectorStats.setTotalFiles(
                codeInspectorStats.getTotalFiles() + codeInspectorProjectStats.getTotalFiles()
        );

        codeInspectorStats.setTotalOutliers(
                codeInspectorStats.getTotalOutliers() + codeInspectorProjectStats.getTotalOutliers()
        );

        codeInspectorStats.setTotalPrioritizedFiles(
                codeInspectorStats.getTotalPrioritizedFiles() + codeInspectorProjectStats.getTotalPrioritizedFiles()
        );

        codeInspectorStatsRepository.save(codeInspectorStats);
    }


}
