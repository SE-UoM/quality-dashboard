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
@RequestMapping("/api/organization-analysis")
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
    
}
