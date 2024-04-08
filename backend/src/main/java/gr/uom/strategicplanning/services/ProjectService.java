package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private ProjectStatsService projectStatsService;
    private OrganizationService organizationService;

    @Autowired
    public ProjectService(
            ProjectRepository projectRepository,
            ProjectStatsService projectStatsService,
            OrganizationService organizationService
    ) {
        this.projectStatsService = projectStatsService;
        this.projectRepository = projectRepository;
        this.organizationService = organizationService;
    }

    public void populateProjectStats(Project project) throws IOException {
        project.setProjectStats(projectStatsService.populateProjectStats(project));
        saveProject(project);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void authorizeProjectForAnalysis(Long id) throws Exception {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(ProjectStatus.ANALYSIS_READY);
        projectRepository.save(project);
    }

    public void unauthorizeProjectForAnalysis(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(ProjectStatus.ANALYSIS_SKIPPED);
        saveProject(project);
    }

    public Collection<Project> getProjectsByOrganization(Long organizationId) {
        // First make sure that the organization exists
        boolean organizationExists = organizationService.organizationExistsById(organizationId);

        if (!organizationExists) {
            throw new EntityNotFoundException("Organization with id " + organizationId + " not found");
        }

        return projectRepository.findAllByOrganizationId(organizationId);
    }

    public Collection<Project> getOrganizationProjectsByStatus(Long orgId, String statusString) {
        // Turn the string into a ProjectStatus
        ProjectStatus status = ProjectStatus.valueOf(statusString);
        return projectRepository.findAllByOrganizationIdAndStatus(orgId, status);
    }

}
