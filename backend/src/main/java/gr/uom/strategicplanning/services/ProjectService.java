package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private ProjectStatsService projectStatsService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectStatsService projectStatsService) {
        this.projectStatsService = projectStatsService;
        this.projectRepository = projectRepository;
    }

    public void populateProject(Project project) {

        project.setTotalCommits(project.getCommits().size());
        project.setTotalDevelopers(project.getDevelopers().size());
        project.setTotalLanguages(project.getLanguages().size());

        project.setProjectStats(projectStatsService.populateProjectStats(project));
        saveProject(project);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void authorizeProjectForAnalysis(Long id) throws Exception {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(ProjectStatus.ANALYSIS_READY);
    }

    public void unauthorizeProjectForAnalysis(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(ProjectStatus.ANALYSIS_SKIPPED);
        saveProject(project);
    }
}
