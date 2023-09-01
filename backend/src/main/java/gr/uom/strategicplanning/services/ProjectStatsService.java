package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.repositories.ProjectStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ProjectStatsService {

    private ProjectStatsRepository projectStatsRepository;

    private SonarApiClient sonarApiClient = new SonarApiClient();

    @Autowired
    public ProjectStatsService(ProjectStatsRepository projectStatsRepository) {
        this.projectStatsRepository = projectStatsRepository;
    }

    public ProjectStats populateProjectStats(Project project) throws IOException {
        ProjectStats projectStats = new ProjectStats();

        int totalLoC = sonarApiClient.retrieveDataFromMeasures(project, "ncloc");
        projectStats.setTotalLoC(totalLoC);

        int totalFiles = sonarApiClient.retrieveDataFromMeasures(project, "files");
        projectStats.setTotalFiles(totalFiles);

        int totalTechDebt = sonarApiClient.retrieveDataFromMeasures(project, "sqale_index");
        projectStats.setTechDebt(totalTechDebt);

        double techDebtPerLoC = (double) totalTechDebt / totalLoC;
        projectStats.setTechDebtPerLoC(techDebtPerLoC);

        int totalCodeSmells = sonarApiClient.retrieveDataFromMeasures(project, "code_smells");
        projectStats.setTotalCodeSmells(totalCodeSmells);

        projectStats.setProject(project);
        saveProjectStats(projectStats);

        return projectStats;
    }


    public void saveProjectStats(ProjectStats projectStats) {
        projectStatsRepository.save(projectStats);
    }
}
