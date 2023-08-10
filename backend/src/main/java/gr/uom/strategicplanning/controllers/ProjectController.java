package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.ProjectResponse;
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
@RequestMapping("/projects")
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

    @GetMapping("/byname/{name}")
    public ResponseEntity<ProjectResponse> getProjectByName(@PathVariable String name) {
        Optional<Project> projectOptional = projectRepository.findByName(name);
        return projectOptional.map(projectResponse -> ResponseEntity.ok(new ProjectResponse(projectResponse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody Project project) {
        Project createdProject = projectRepository.save(project);
        ProjectResponse projectResponse = new ProjectResponse(createdProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            Project existingProject = projectOptional.get();
            existingProject.setRepoUrl(updatedProject.getRepoUrl());
            existingProject.setForks(updatedProject.getForks());
            existingProject.setStars(updatedProject.getStars());
            existingProject.setCommits(updatedProject.getCommits());
            existingProject.setTotalDevelopers(updatedProject.getTotalDevelopers());
            existingProject.setTotalCommits(updatedProject.getTotalCommits());
            existingProject.setLanguages(updatedProject.getLanguages());
            existingProject.setTotalLanguages(updatedProject.getTotalLanguages());
            existingProject.setDevelopers(updatedProject.getDevelopers());
            Project savedProject = (Project) projectRepository.save(existingProject);
            return ResponseEntity.ok(savedProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            projectRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
