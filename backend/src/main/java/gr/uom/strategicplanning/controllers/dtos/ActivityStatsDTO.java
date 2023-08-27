package gr.uom.strategicplanning.controllers.dtos;

import gr.uom.strategicplanning.models.stats.ActivityStats;

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
        this.commitsPerDay = activityStats.getCommitsPerDay();
        this.locAddedPerDay = activityStats.getLocAddedPerDay();
        this.filesAddedPerDay = activityStats.getFilesAddedPerDay();
        this.projectsAddedPerDay = activityStats.getProjectsAddedPerDay();
        this.averageLoC = activityStats.getAverageLoC();
        this.organizationAnalysisId = activityStats.getOrganizationAnalysis().getId();
    }
}
