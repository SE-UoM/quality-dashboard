package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.CommitResponse;
import gr.uom.strategicplanning.controllers.responses.DeveloperResponse;
import gr.uom.strategicplanning.controllers.responses.LanguageResponse;
import gr.uom.strategicplanning.controllers.responses.ProjectResponse;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> projectResponses = projects.stream()
                .map(ProjectResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        return projectOptional.map(projectResponse -> ResponseEntity.ok(new ProjectResponse(projectResponse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{project_id}/commits")
    public ResponseEntity<List<CommitResponse>> getProjectCommits(@PathVariable String project_id) {
        Optional<Project> projectOptional = projectRepository.findById(Long.parseLong(project_id));

        if (projectOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Project project = projectOptional.get();
        return ResponseEntity.ok(project.getCommits().stream()
               .map(CommitResponse::new)
               .collect(Collectors.toList()));
    }

    @GetMapping("/{project_id}/commits/{commit_id}")
    public ResponseEntity<CommitResponse> getProjectCommitById(@PathVariable String project_id, @PathVariable String commit_id) {
        Optional<Project> projectOptional = projectRepository.findById(Long.parseLong(project_id));

        if (projectOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Project project = projectOptional.get();
        Optional<Commit> commitOptional = project.getCommits().stream()
               .filter(commit -> commit.getId().equals(Long.parseLong(commit_id)))
               .findFirst();

        return commitOptional.map(commitResponse -> ResponseEntity.ok(new CommitResponse(commitResponse)))
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{project_id}/developers")
    public ResponseEntity<List<DeveloperResponse>> getProjectDevelopers(@PathVariable String project_id) {
        Optional<Project> projectOptional = projectRepository.findById(Long.parseLong(project_id));

        if (projectOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Project project = projectOptional.get();
        List<DeveloperResponse> projectDevelopers = project.getDevelopers().stream()
               .map(DeveloperResponse::new)
               .collect(Collectors.toList());

        return ResponseEntity.ok(projectDevelopers);
    }

    @GetMapping("/{project_id}/developers/{developer_id}")
    public ResponseEntity<DeveloperResponse> getProjectDeveloperById(@PathVariable String project_id, @PathVariable String developer_id) {
        Optional<Project> projectOptional = projectRepository.findById(Long.parseLong(project_id));

        if (projectOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Project project = projectOptional.get();
        Optional<DeveloperResponse> developerOptional = project.getDevelopers().stream()
               .filter(developer -> developer.getId().equals(Long.parseLong(developer_id)))
               .map(DeveloperResponse::new)
               .findFirst();

        return developerOptional.map(developerResponse -> ResponseEntity.ok(developerResponse))
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{project_id}/languages")
    public ResponseEntity<List<LanguageResponse>> getProjectLanguages(@PathVariable String project_id) {
        Optional<Project> projectOptional = projectRepository.findById(Long.parseLong(project_id));

        if (projectOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Project project = projectOptional.get();
        List<LanguageResponse> projectLanguages = project.getLanguages().stream()
               .map(language -> new LanguageResponse(language))
               .collect(Collectors.toList());

        return ResponseEntity.ok(projectLanguages);
    }

}
