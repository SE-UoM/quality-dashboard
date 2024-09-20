package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.external.CodeInspectorProjectStats;
import gr.uom.strategicplanning.models.stats.CodeInspectorStats;
import gr.uom.strategicplanning.repositories.CodeInspectorProjectStatsRepository;
import gr.uom.strategicplanning.repositories.CodeInspectorStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CodeInspectorStatsService {
    private CodeInspectorProjectStatsRepository codeInspectorProjectStatsRepository;
    private CodeInspectorStatsRepository codeInspectorStatsRepository;

    @Autowired
    public CodeInspectorStatsService(CodeInspectorProjectStatsRepository codeInspectorProjectStatsRepository, CodeInspectorStatsRepository codeInspectorStatsRepository) {
        this.codeInspectorProjectStatsRepository = codeInspectorProjectStatsRepository;
        this.codeInspectorStatsRepository = codeInspectorStatsRepository;
    }

    public void updateHotspotAnalysisProjectStats(Project project, Map<String, Object> response) {
        Map<String, Object> analysis = (Map<String, Object>) response.get("analysis");
        log.info("Updating Hotspot Analysis Project Stats");

        Optional<CodeInspectorProjectStats> codeInspectorProjectStats = codeInspectorProjectStatsRepository.findByProjectId(project.getId());

        if (codeInspectorProjectStats.isEmpty()) throw new RuntimeException("Project not found in CodeInspectorProjectStats");

        Collection<Map> hotspots = (Collection<Map>) analysis.get("prioritized_files");
        int totalHotspots = hotspots.size();

        // Parse the hotspot files list and count the number of high, medium, normal and low priority hotspots
        int highPriorityHotspots = 0;
        int mediumPriorityHotspots = 0;
        int normalPriorityHotspots = 0;
        int lowPriorityHotspots = 0;

        for (Map hotspot : hotspots) {
            String priority = (String) hotspot.get("priority");

            if (priority.equalsIgnoreCase("HIGH")) highPriorityHotspots++;
            else if (priority.equalsIgnoreCase("MEDIUM")) mediumPriorityHotspots++;
            else if (priority.equalsIgnoreCase("NORMAL")) normalPriorityHotspots++;
            else if (priority.equalsIgnoreCase("LOW")) lowPriorityHotspots++;
        }

        Collection<Map> outliers = (Collection<Map>) analysis.get("outliers");
        int totalOutliers = outliers.size();

        CodeInspectorProjectStats updatedProjectStats = codeInspectorProjectStats.get();
        updatedProjectStats.setProjectUrl(project.getRepoUrl());
        updatedProjectStats.setProjectName(project.getName());
        updatedProjectStats.setFromDate((String) analysis.get("from_date"));
        updatedProjectStats.setToDate((String) analysis.get("to_date"));
        updatedProjectStats.setAverageChurn((Double) analysis.get("avg_churn"));
        updatedProjectStats.setTotalOutliers(totalOutliers);
        updatedProjectStats.setTotalHotspots(totalHotspots);
        updatedProjectStats.setHighPriorityHotspots(highPriorityHotspots);
        updatedProjectStats.setMediumPriorityHotspots(mediumPriorityHotspots);
        updatedProjectStats.setNormalPriorityHotspots(normalPriorityHotspots);
        updatedProjectStats.setLowPriorityHotspots(lowPriorityHotspots);

        codeInspectorProjectStatsRepository.save(updatedProjectStats);
    }

    public void updateOrganizationStats(Organization org) {
        log.info("Updating Organization Stats");

        Long organizationAnalysisId = org.getOrganizationAnalysis().getId();
        Optional<CodeInspectorStats> codeInspectorStats = codeInspectorStatsRepository.findByOrganizationAnalysisId(organizationAnalysisId);

        if (codeInspectorStats.isEmpty()) throw new RuntimeException("Organization not found in CodeInspectorStats");

        CodeInspectorStats updatedStats = codeInspectorStats.get();

        List<Project> organizationProjects = org.getProjects();

        int totalHotspots = 0;
        int totalOutliers = 0;

        for(Project project : organizationProjects) {
            Long projectID = project.getId();
            Optional<CodeInspectorProjectStats> projectStats = codeInspectorProjectStatsRepository.findByProjectId(projectID);

            if (projectStats.isEmpty()) throw new RuntimeException("Project not found in CodeInspectorProjectStats");

            totalHotspots += projectStats.get().getTotalHotspots();
            totalOutliers += projectStats.get().getTotalOutliers();

            updatedStats.addGitUrl(project.getRepoUrl());
        }

        updatedStats.setTotalHotspots(totalHotspots);
        updatedStats.setTotalOutliers(totalOutliers);

        codeInspectorStatsRepository.save(updatedStats);
    }
}
