package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.dtos.GeneralStatsDTO;
import gr.uom.strategicplanning.controllers.responses.*;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
            try {
                List<Organization> organizations = organizationRepository.findAll();
                List<OrganizationResponse> organizationResponses = organizations.stream()
                        .map(OrganizationResponse::new)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(organizationResponses);
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);
            Organization organization = organizationOptional.orElseThrow(() -> new RuntimeException("Organization not found"));

            OrganizationResponse organizationResponse = new OrganizationResponse(organization);
            return ResponseEntity.ok(organizationResponse);
        }
    @GetMapping("/{id}/projects-info")
    public ResponseEntity<Collection<Map>> getOrganizationProjectsInfo(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            Collection<Project> organizationProjects = organization.getProjects();
            Collection<Map> projectsInfoResponse = new ArrayList<>();

            for (Project project : organizationProjects) {
                String projectName = project.getName();
                int projectCommits = project.getTotalCommits();
                int getProjectStars = project.getStars();
                int projectForks = project.getForks();

                Map<String, Object> projectInfoMap = new HashMap<>();
                projectInfoMap.put("name", projectName);
                projectInfoMap.put("totalContributions", projectCommits);
                projectInfoMap.put("stars", getProjectStars);
                projectInfoMap.put("forks", projectForks);

                projectsInfoResponse.add(projectInfoMap);
            }

            return ResponseEntity.ok(projectsInfoResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/project-names")
    public ResponseEntity<Collection<Map>> getOrganizationProjectNames(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            Collection<Project> organizationProjects = organization.getProjects();
            Collection<Map> projectsInfoResponse = new ArrayList<>();

            for (Project project : organizationProjects) {
                String projectName = project.getName();

                Map<String, Object> projectInfoMap = new HashMap<>();
                projectInfoMap.put("name", projectName);

                projectsInfoResponse.add(projectInfoMap);
            }

            return ResponseEntity.ok(projectsInfoResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/organization-analysis/general-stats")
    public ResponseEntity<GeneralStatsDTO> getOrganizationAnalysisGeneralStats(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            GeneralStats generalStats = organizationAnalysis.getGeneralStats();

            GeneralStatsDTO generalStatsDTO = new GeneralStatsDTO(generalStats);

            return ResponseEntity.ok(generalStatsDTO);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/organization-analysis")
    public ResponseEntity<OrganizationAnalysisResponse> getOrganizationAnalysis(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);

            if (organizationOptional.isEmpty())
                return ResponseEntity.notFound().build();

            Organization organization = organizationOptional.get();

            OrganizationAnalysisResponse organizationAnalysisResponse = new OrganizationAnalysisResponse(organization.getOrganizationAnalysis());

            return ResponseEntity.ok(organizationAnalysisResponse);
        }

    @GetMapping("/{id}/top_developers")
    public ResponseEntity<List<DeveloperResponse>> getTopDevelopersByOrganizationId(@PathVariable Long id) {
            Organization organization = organizationRepository.findById(id).orElse(null);
            if (organization == null) {
                return ResponseEntity.notFound().build();
            }

            Collection<Developer> projectDevelopers = organization.getProjects().stream()
                    .flatMap(project -> project.getDevelopers().stream())
                    .collect(Collectors.toList());

            List<Developer> topDeveloper = projectDevelopers.stream()
                    .sorted(Comparator.comparingDouble(Developer::getCodeSmellsPerCommit))
                    .collect(Collectors.toList());

            List<DeveloperResponse> developerResponses = topDeveloper.stream()
                    .map(DeveloperResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(developerResponses);
        }
}
