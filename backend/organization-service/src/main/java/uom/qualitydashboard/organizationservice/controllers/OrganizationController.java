package uom.qualitydashboard.organizationservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.qualitydashboard.organizationservice.models.Organization;
import uom.qualitydashboard.organizationservice.services.OrganizationService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping("/all")
    public ResponseEntity<Collection<Organization>> getOrganizations() {
        Collection<Organization> organizations = organizationService.getOrganizations();

        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/id/{organizationId}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable(name = "organizationId") Long organizationId) {
        Optional<Organization> organization = organizationService.getOrganizationById(organizationId);

        if (organization.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(organization.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization savedOrganization = organizationService.saveOrganization(organization);

        return ResponseEntity.ok(savedOrganization);
    }
}
