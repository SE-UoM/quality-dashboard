package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.repositories.ProjectStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectStatsService {

    private ProjectStatsRepository projectStatsRepository;

    @Autowired
    public ProjectStatsService(ProjectStatsRepository projectStatsRepository) {
        this.projectStatsRepository = projectStatsRepository;
    }

    public ProjectStats populateProjectStats(Project project) {
        ProjectStats projectStats = new ProjectStats();
        project.getCommits().forEach((commit -> {
            projectStats.setTotalLoC(projectStats.getTotalLoC() + commit.getTotalLoC());
            projectStats.setTotalFiles(projectStats.getTotalFiles() + commit.getTotalFiles());
            projectStats.setTotalCodeSmells(projectStats.getTotalCodeSmells() + commit.getTotalCodeSmells());
            projectStats.setTechDebt((int) (projectStats.getTechDebt() + commit.getTechnicalDebt()));
            projectStats.setTechDebtPerLoC(projectStats.getTechDebtPerLoC() + commit.getTechDebtPerLoC());
//            projectStats.setTotalLanguages(projectStats.getTotalLanguages() + commit.getLanguages().size());
        }));

        saveProjectStats(projectStats);

        return projectStats;
    }

    public void saveProjectStats(ProjectStats projectStats) {
        projectStatsRepository.save(projectStats);
    }
}
