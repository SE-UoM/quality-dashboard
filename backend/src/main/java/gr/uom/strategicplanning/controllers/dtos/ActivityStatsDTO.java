package gr.uom.strategicplanning.controllers.dtos;

import gr.uom.strategicplanning.models.stats.ActivityStats;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ActivityStatsDTO {
    private Long id;
    private float commitsPerDay = -1;
    private float locAddedPerDay = -1;
    private float filesAddedPerDay = -1;
    private float projectsAddedPerDay = -1;
    private float averageLoC = -1;
    private Long organizationAnalysisId;

    public ActivityStatsDTO(ActivityStats activityStats) {
        this.id = activityStats.getId();
        this.commitsPerDay = activityStats.getCommitsPerAnalysis();
        this.locAddedPerDay = activityStats.getLocAddedPerAnalysis();
        this.filesAddedPerDay = activityStats.getFilesAddedPerAnalysis();
        this.projectsAddedPerDay = activityStats.getProjectsAddedPerAnalysis();
        this.averageLoC = activityStats.getAverageLoC();
        this.organizationAnalysisId = activityStats.getOrganizationAnalysis().getId();
    }
}
