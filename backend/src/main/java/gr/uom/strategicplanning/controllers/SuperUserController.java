package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.OrganizationResponse;
import gr.uom.strategicplanning.controllers.responses.ProjectResponse;
import gr.uom.strategicplanning.controllers.responses.UserResponse;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/superuser")
public class SuperUserController {

    //TODO: Implement super user business logic

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private OrganizationAnalysisRepository organizationAnalysisRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping("/organization")
    public ResponseEntity<OrganizationResponse> createOrganization(@RequestBody Organization organization) {
        Organization createdOrganization = organizationRepository.save(organization);
        OrganizationResponse organizationResponse = new OrganizationResponse(createdOrganization);
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationResponse);
    }

    @GetMapping("/organization/{id}/users")
    public ResponseEntity<List<UserResponse>> getOrganizationUsers(@PathVariable Long id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);

        if (organizationOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Organization organization = organizationOptional.get();

        List<UserResponse> userResponses = organization.getUsers().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }

    @PutMapping("/organization/{id}")
    public ResponseEntity<OrganizationResponse> updateOrganization(@PathVariable Long id, @RequestBody Organization updatedOrganization) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            Organization existingOrganization = organizationOptional.get();
            existingOrganization.setName(updatedOrganization.getName());
            // Set other fields
            Organization savedOrganization = organizationRepository.save(existingOrganization);
            OrganizationResponse organizationResponse = new OrganizationResponse(savedOrganization);
            return ResponseEntity.ok(organizationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/developer/{id}")
    public ResponseEntity<Developer> updateDeveloper(@PathVariable Long id, @RequestBody Developer updatedDeveloper) {
        Optional<Developer> developerOptional = developerRepository.findById(id);
        if (developerOptional.isPresent()) {
            Developer existingDeveloper = developerOptional.get();
            existingDeveloper.setName(updatedDeveloper.getName());
            existingDeveloper.setGithubUrl(updatedDeveloper.getGithubUrl());
            existingDeveloper.setTotalCommits(updatedDeveloper.getTotalCommits());
            Developer savedDeveloper = (Developer) developerRepository.save(existingDeveloper);
            return ResponseEntity.ok(savedDeveloper);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/organization-analysis/{id}")
    public ResponseEntity<OrganizationAnalysis> updateOrganizationAnalysis(@PathVariable Long id, @RequestBody OrganizationAnalysis updatedOrganizationAnalysis) {
        Optional<OrganizationAnalysis> organizationAnalysisOptional = organizationAnalysisRepository.findById(id);
        if (organizationAnalysisOptional.isPresent()) {
            OrganizationAnalysis existingOrganizationAnalysis = organizationAnalysisOptional.get();
            existingOrganizationAnalysis.setOrgName(updatedOrganizationAnalysis.getOrgName());
            existingOrganizationAnalysis.setAnalysisDate(updatedOrganizationAnalysis.getAnalysisDate());
            existingOrganizationAnalysis.setGeneralStats(updatedOrganizationAnalysis.getGeneralStats());
            existingOrganizationAnalysis.setTechDebtStats(updatedOrganizationAnalysis.getTechDebtStats());
            existingOrganizationAnalysis.setActivityStats(updatedOrganizationAnalysis.getActivityStats());
            existingOrganizationAnalysis.setMostStarredProject(updatedOrganizationAnalysis.getMostStarredProject());
            existingOrganizationAnalysis.setMostForkedProject(updatedOrganizationAnalysis.getMostForkedProject());
            OrganizationAnalysis savedOrganizationAnalysis = (OrganizationAnalysis) organizationAnalysisRepository.save(existingOrganizationAnalysis);
            return ResponseEntity.ok(savedOrganizationAnalysis);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/language/{id}")
    public ResponseEntity<Language> updateLanguage(@PathVariable Long id, @RequestBody Language updatedLanguage) {
        Optional<Language> languageOptional = languageRepository.findById(id);
        if (languageOptional.isPresent()) {
            Language existingLanguage = languageOptional.get();
            existingLanguage.setName(updatedLanguage.getName());
            existingLanguage.setImageUrl(updatedLanguage.getImageUrl());
            Language savedLanguage = (Language) languageRepository.save(existingLanguage);
            return ResponseEntity.ok(savedLanguage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/project/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        Optional<Project> projectOptional = projectRepository.findById(id);

        if (projectOptional.isEmpty())
            return ResponseEntity.notFound().build();

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
    }



    @DeleteMapping("/project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);

        if (projectOptional.isEmpty())
            return ResponseEntity.notFound().build();

        projectRepository.delete(projectOptional.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/organization-analysis/{id}")
    public ResponseEntity<Void> deleteOrganizationAnalysis(@PathVariable Long id) {
        Optional<OrganizationAnalysis> organizationAnalysisOptional = organizationAnalysisRepository.findById(id);
        if (organizationAnalysisOptional.isPresent()) {
            organizationAnalysisRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/language/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        Optional<Language> languageOptional = languageRepository.findById(id);
        if (languageOptional.isPresent()) {
            languageRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/developer/{id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        Optional<Developer> developerOptional = developerRepository.findById(id);
        if (developerOptional.isPresent()) {
            developerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/organization/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            organizationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/commit/{id}")
    public ResponseEntity<Void> deleteCommit(@PathVariable Long id) {
        Optional<Commit> commit = commitRepository.findById(id);
        if (commit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        commitRepository.delete(commit.get());
        return ResponseEntity.noContent().build();
    }

}
