package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import org.springframework.stereotype.Service;

@Service
public class ProjectStatsService {
    public ProjectStats populateProjectStats(Project project) {
        ProjectStats projectStats = new ProjectStats();
        project.getCommits().forEach((commit -> {
            projectStats.setTotalLoC(projectStats.getTotalLoC().doubleValue() + commit.getTotalLoC().doubleValue());
            projectStats.setTotalFiles(projectStats.getTotalFiles().doubleValue() + commit.getTotalFiles().doubleValue());
            projectStats.setTotalCodeSmells(projectStats.getTotalCodeSmells().doubleValue() + commit.getTotalCodeSmells().doubleValue());
            projectStats.setTechDebt(projectStats.getTechDebt().doubleValue() + commit.getTechnicalDebt().doubleValue());
            projectStats.setTechDebtPerLoC(projectStats.getTechDebtPerLoC().doubleValue() + commit.getTechDebtPerLoC().doubleValue());
            projectStats.setTotalLanguages(projectStats.getTotalLanguages().doubleValue() + commit.getLanguages().size());
        }));

        return projectStats;
    }
}
