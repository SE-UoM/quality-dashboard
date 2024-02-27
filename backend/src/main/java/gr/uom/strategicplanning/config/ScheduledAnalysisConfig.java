package gr.uom.strategicplanning.config;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import gr.uom.strategicplanning.services.AnalysisService;
import gr.uom.strategicplanning.services.OrganizationAnalysisService;
import gr.uom.strategicplanning.services.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@Slf4j
public class ScheduledAnalysisConfig {
    public static final long ONE_HOUR = 3600000;
    public static final long TEN_SECONDS = 10000;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationAnalysisService organizationAnalysisService;

    @Scheduled(fixedRate = ONE_HOUR)
    public void runScheduledAnalysis() throws Exception {

        try {
            List<Project> projects = projectRepository.findAllByStatus(ProjectStatus.ANALYSIS_NOT_STARTED);

            log.info("Starting analysis for " + projects.size() + " projects");

            for (Project project : projects) {
                startProjectAnalysis(project);
            }
        }
        catch (Exception e) {
            log.error("Analysis failed", e);
            updateProjectStatus(projectRepository.findAllByStatus(ProjectStatus.ANALYSIS_IN_PROGRESS).get(0), ProjectStatus.ANALYSIS_NOT_STARTED);

            e.printStackTrace();
        }
    }

    private int startProjectAnalysis(Project project) throws Exception {
        int ANALYSIS_NOT_STARTED = 0;
        int ANALYSIS_DONE = 1;

        if (project.getStatus() == ProjectStatus.ANALYSIS_IN_PROGRESS) return ANALYSIS_NOT_STARTED;

        log.info("Starting analysis for " + project.getRepoUrl());

        updateProjectStatus(project, ProjectStatus.ANALYSIS_IN_PROGRESS);

        log.info("Fetching github data for " + project.getRepoUrl());
        analysisService.fetchGithubData(project);

        Organization organization = project.getOrganization();

        log.info("Saving organization " + organization.getName());
        organizationService.saveOrganization(organization);

        log.info("Starting analysis for " + project.getName());
        // Calculate how long it takes to analyze a project
        long start = System.currentTimeMillis();

        analysisService.startAnalysis(project);

        long end = System.currentTimeMillis();
        int timeInMinutes = (int) ((end - start) / ONE_HOUR);

        log.info("Analysis for " + project.getName() + " finished in " + timeInMinutes + " minutes");

        updateProjectStatus(project, ProjectStatus.ANALYSIS_COMPLETED);

        organizationAnalysisService.updateOrganizationAnalysis(organization);

        project.setStatus(ProjectStatus.ANALYSIS_IN_PROGRESS);
        projectRepository.save(project);

        return ANALYSIS_DONE;
    }

    private void updateProjectStatus(Project project, ProjectStatus status) {
        project.setStatus(status);
        projectRepository.save(project);
    }
}
