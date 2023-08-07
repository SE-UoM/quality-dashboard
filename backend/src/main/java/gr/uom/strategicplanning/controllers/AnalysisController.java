package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import gr.uom.strategicplanning.services.AnalysisService;
import gr.uom.strategicplanning.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Autowired
    public AnalysisController(AnalysisService analysisService, ProjectService projectService, ProjectRepository projectRepository) {
        this.analysisService = analysisService;
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/start")
    public void startAnalysis(@RequestParam("github_url") String githubUrl) throws Exception {
        Project project = new Project();
        Optional<Project> projectOptional = projectRepository.findFirstByRepoUrl(githubUrl);

        if (projectOptional.isEmpty())
            project.setRepoUrl(githubUrl);
        else
            project = projectOptional.get();
        
        analysisService.fetchGithubData(project);

        projectService.saveProject(project);

        if (project.canBeAnalyzed())
            analysisService.startAnalysis(project);
        else
            project.setStatus(ProjectStatus.ANALYSIS_TO_BE_REVIEWED);
    }
}
