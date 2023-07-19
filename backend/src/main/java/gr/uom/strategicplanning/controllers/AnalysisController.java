package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.Project;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import gr.uom.strategicplanning.services.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private AnalysisService analysisService;
    private ProjectRepository projectRepository;

    @Autowired
    public AnalysisController(AnalysisService analysisService, ProjectRepository projectRepository) {
        this.analysisService = analysisService;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/start")
    public void startAnalysis(@RequestParam("github_url") String githubUrl) throws Exception {
        Project project = new Project();
        project.setRepoUrl(githubUrl);

        analysisService.fetchGithubData(project);

        projectRepository.save(project);

        if (project.canBeAnalyzed())
            analysisService.startAnalysis(project);
        else
            project.setStatus(ProjectStatus.ANALYSIS_TO_BE_REVIEWED);
    }
}
