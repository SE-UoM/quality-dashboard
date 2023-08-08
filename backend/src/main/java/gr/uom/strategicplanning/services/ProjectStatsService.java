package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import org.springframework.stereotype.Service;

@Service
public class ProjectStatsService {
    public ProjectStats populateProjectStats(Project project) {
        ProjectStats projectStats = new ProjectStats();
        project.getCommits().forEach((commit -> {
            projectStats.setTotalLoC(projectStats.getTotalLoC() + commit.getTotalLoC());
            projectStats.setTotalFiles(projectStats.getTotalFiles() + commit.getTotalFiles());
            projectStats.setTotalCodeSmells(projectStats.getTotalCodeSmells() + commit.getTotalCodeSmells());
            projectStats.setTechDebt((int) (projectStats.getTechDebt() + commit.getTechnicalDebt()));
            projectStats.setTechDebtPerLoC(projectStats.getTechDebtPerLoC() + commit.getTechDebtPerLoC());
            projectStats.setTotalLanguages(projectStats.getTotalLanguages() + commit.getLanguages().size());
        }));

        return projectStats;
    }
}
