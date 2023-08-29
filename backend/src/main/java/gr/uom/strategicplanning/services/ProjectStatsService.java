package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.repositories.ProjectStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectStatsService {

    private ProjectStatsRepository projectStatsRepository;

    @Autowired
    public ProjectStatsService(ProjectStatsRepository projectStatsRepository) {
        this.projectStatsRepository = projectStatsRepository;
    }

    public ProjectStats populateProjectStats(Project project) {
        ProjectStats projectStats = new ProjectStats();
        Commit latestCommit = null;

        for(Commit commit: project.getCommits()) {
            Date commitDate = commit.getCommitDate();
            if(latestCommit == null || commitDate.after(latestCommit.getCommitDate())) {
                latestCommit = commit;
            }
        }

        if(latestCommit != null) {
            projectStats.setTotalLoC(latestCommit.getTotalLoC());
            projectStats.setTotalFiles(latestCommit.getTotalFiles());
            projectStats.setTechDebt((int) latestCommit.getTechnicalDebt());
            projectStats.setTechDebtPerLoC(latestCommit.getTechDebtPerLoC());
//            projectStats.setTotalLanguages(latestCommit.getLanguages().size());
            projectStats.setTotalCodeSmells(latestCommit.getTotalCodeSmells());
            projectStats.setTotalLanguages(-1);
        }

        projectStats.setProject(project);
        saveProjectStats(projectStats);

        return projectStats;
    }

    public void saveProjectStats(ProjectStats projectStats) {
        projectStatsRepository.save(projectStats);
    }
}
