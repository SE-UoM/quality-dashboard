package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.OrganizationAnalysis;
import gr.uom.strategicplanning.repositories.OrganizationAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organization-analysis")
public class OrganizationAnalysisController {

    @Autowired
    private OrganizationAnalysisRepository organizationAnalysisRepository;


    @GetMapping
    public ResponseEntity<List<OrganizationAnalysis>> getAllOrganizationAnalysis() {
        List<OrganizationAnalysis> organizationAnalyses = organizationAnalysisRepository.findAll();
        return ResponseEntity.ok(organizationAnalyses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationAnalysis> getOrganizationAnalysisById(@PathVariable Long id) {
        Optional<OrganizationAnalysis> organizationAnalysisOptional = organizationAnalysisRepository.findById(id);
        return organizationAnalysisOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrganizationAnalysis> createOrganizationAnalysis(@RequestBody OrganizationAnalysis organizationAnalysis) {
        OrganizationAnalysis createdOrganizationAnalysis = (OrganizationAnalysis) organizationAnalysisRepository.save(organizationAnalysis);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrganizationAnalysis);
    }

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizationAnalysis(@PathVariable Long id) {
        Optional<OrganizationAnalysis> organizationAnalysisOptional = organizationAnalysisRepository.findById(id);
        if (organizationAnalysisOptional.isPresent()) {
            organizationAnalysisRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
