package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.OrganizationAnalysisResponse;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.repositories.OrganizationAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/organization-analysis")
public class OrganizationAnalysisController {

    @Autowired
    private OrganizationAnalysisRepository organizationAnalysisRepository;


    @GetMapping
    public ResponseEntity<List<OrganizationAnalysisResponse>> getAllOrganizationAnalysis() {
        List<OrganizationAnalysis> organizationAnalyses = organizationAnalysisRepository.findAll();
        List<OrganizationAnalysisResponse> organizationAnalysisResponses = organizationAnalyses.stream()
                .map(OrganizationAnalysisResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(organizationAnalysisResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationAnalysisResponse> getOrganizationAnalysisById(@PathVariable Long id) {
        Optional<OrganizationAnalysis> organizationAnalysisOptional = organizationAnalysisRepository.findById(id);
        return organizationAnalysisOptional.map(organizationAnalysisResponse -> ResponseEntity.ok(new OrganizationAnalysisResponse(organizationAnalysisResponse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrganizationAnalysisResponse> createOrganizationAnalysis(@RequestBody OrganizationAnalysis organizationAnalysis) {
        OrganizationAnalysis createdOrganizationAnalysis = organizationAnalysisRepository.save(organizationAnalysis);
        OrganizationAnalysisResponse organizationAnalysisResponse = new OrganizationAnalysisResponse(createdOrganizationAnalysis);
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationAnalysisResponse);
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
