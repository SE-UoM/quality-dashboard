package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.OrganizationResponse;
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
    @RequestMapping("/organizations")
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

        @PostMapping
        public ResponseEntity<OrganizationResponse> createOrganization(@RequestBody Organization organization) {
            Organization createdOrganization = organizationRepository.save(organization);
            OrganizationResponse organizationResponse = new OrganizationResponse(createdOrganization);
            return ResponseEntity.status(HttpStatus.CREATED).body(organizationResponse);
        }

        @PutMapping("/{id}")
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

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);
            if (organizationOptional.isPresent()) {
                organizationRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
