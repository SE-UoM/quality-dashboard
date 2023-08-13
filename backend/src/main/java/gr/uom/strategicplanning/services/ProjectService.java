package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AnalysisService analysisService;

    private final ProjectStatsService projectStatsService = new ProjectStatsService();

    public void populateProject(Project project) {
        HashSet<Developer> developers = new HashSet<>();
        ArrayList<LanguageStats> languages = new ArrayList<>();

        project.getCommits().forEach(commit -> {
            languages.addAll(commit.getLanguages());
            developers.add(commit.getDeveloper());
        });

        project.setTotalCommits(project.getCommits().size());;
        project.setLanguages(languages);
        project.setDevelopers(developers);
        project.setTotalDevelopers(developers.size());
        project.setTotalLanguages(languages.size());

        project.setProjectStats(projectStatsService.populateProjectStats(project));
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void authorizeProjectForAnalysis(Long id) throws Exception {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(ProjectStatus.ANALYSIS_READY);
        analysisService.startAnalysis(project);
        saveProject(project);
    }

    public void unauthorizeProjectForAnalysis(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(ProjectStatus.ANALYSIS_SKIPPED);
        saveProject(project);
    }
}
