package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.Organization;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {


    @Autowired
    private OrganizationRepository organizationRepository;

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        return organizationOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization createdOrganization = (Organization) organizationRepository.save(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrganization);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Long id, @RequestBody Organization updatedOrganization) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            Organization existingOrganization = organizationOptional.get();
            existingOrganization.setName(updatedOrganization.getName());
            existingOrganization.setUsers(updatedOrganization.getUsers());
            existingOrganization.setProjects(updatedOrganization.getProjects());
            existingOrganization.setOrganizationAnalysis(updatedOrganization.getOrganizationAnalysis());
            Organization savedOrganization = (Organization) organizationRepository.save(existingOrganization);
            return ResponseEntity.ok(savedOrganization);
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
