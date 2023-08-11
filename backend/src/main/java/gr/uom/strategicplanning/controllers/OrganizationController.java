package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.OrganizationAnalysisResponse;
import gr.uom.strategicplanning.controllers.responses.OrganizationResponse;
import gr.uom.strategicplanning.controllers.responses.ProjectResponse;
import gr.uom.strategicplanning.controllers.responses.UserResponse;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
    @RestController
    @RequestMapping("/api/organizations")
    public class OrganizationController {

        @Autowired
        private OrganizationRepository organizationRepository;

        @GetMapping
        public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
            List<Organization> organizations = organizationRepository.findAll();
            List<OrganizationResponse> organizationResponses = organizations.stream()
                    .map(OrganizationResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(organizationResponses);
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);
            Organization organization = organizationOptional.orElseThrow(() -> new RuntimeException("Organization not found"));

            OrganizationResponse organizationResponse = new OrganizationResponse(organization);
            return ResponseEntity.ok(organizationResponse);
        }

        @GetMapping("/{id}/projects")
        public ResponseEntity<List<ProjectResponse>> getOrganizationProjects(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);

            if (!organizationOptional.isEmpty())
            	return ResponseEntity.notFound().build();


            Organization organization = organizationOptional.get();

            List<ProjectResponse> projectResponses = organization.getProjects().stream()
                    .map(ProjectResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(projectResponses);
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


        
    }
