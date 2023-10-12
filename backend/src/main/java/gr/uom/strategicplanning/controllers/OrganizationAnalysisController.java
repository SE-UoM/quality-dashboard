package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.controllers.responses.implementations.OrganizationAnalysisResponse;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.repositories.OrganizationAnalysisRepository;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organization-analysis")
public class OrganizationAnalysisController {

    @Autowired
    private OrganizationAnalysisRepository organizationAnalysisRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @GetMapping
    public ResponseEntity<List<ResponseInterface>> getAllOrganizationAnalysis() {
        List<OrganizationAnalysis> organizationAnalyses = organizationAnalysisRepository.findAll();
        List<ResponseInterface> organizationAnalysisResponses = organizationAnalyses.stream()
                .map(ResponseFactory::createOrganizationAnalysisResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(organizationAnalysisResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseInterface> getOrganizationAnalysisById(@PathVariable Long id) {
        Optional<OrganizationAnalysis> organizationAnalysisOptional = organizationAnalysisRepository.findById(id);
        return organizationAnalysisOptional
                .map(
                        organizationAnalysisResponse ->
                                ResponseEntity
                                        .ok(ResponseFactory.createOrganizationAnalysisResponse(organizationAnalysisResponse))
                )
                .orElse(ResponseEntity.notFound().build());
    }

}
