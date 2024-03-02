package uom.qualitydashboard.githubanalysisservice.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.qualitydashboard.githubanalysisservice.client.GithubApiClient;
import uom.qualitydashboard.githubanalysisservice.client.ProjectSubmissionMicroserviceClient;
import uom.qualitydashboard.githubanalysisservice.models.GithubAnalysis;
import uom.qualitydashboard.githubanalysisservice.models.SubmittedProjectDTO;
import uom.qualitydashboard.githubanalysisservice.services.GithubAnalysisService;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/github-analysis")
public class GithubAnalysisController {
    private final GithubAnalysisService githubAnalysisService;

    @PostMapping("/start")
    public ResponseEntity<?> startAnalysis(@RequestParam(name = "projectId") Long projectId) {
        try {
            GithubAnalysis analysis = githubAnalysisService.startAnalysis(projectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(analysis);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAnalyses() {
        try {
            Collection<GithubAnalysis> analyses = githubAnalysisService.getAllGithubAnalyses();
            return ResponseEntity.status(HttpStatus.OK).body(analyses);
        }
        catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/project/id/{projectId}")
    public ResponseEntity<?> getAnalysisByProjectId(@PathVariable Long projectId) {
        try {
            Collection<GithubAnalysis> analyses = githubAnalysisService.getAnalysisByProjectId(projectId);
            return ResponseEntity.status(HttpStatus.OK).body(analyses);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/project")
    public ResponseEntity<?> getAnalysisByProjectName(@RequestParam(name = "pname") String projectName) {
        try {
            Collection<GithubAnalysis> analysis = githubAnalysisService.getAnalysisByProjectName(projectName);
            return ResponseEntity.status(HttpStatus.OK).body(analysis);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/project/full-name/{projectFullName}")
    public ResponseEntity<?> getAnalysisByProjectFullName(@PathVariable String projectFullName) {
        try {
            Collection<GithubAnalysis> analysis = githubAnalysisService.getAnalysisByProjectFullName(projectFullName);
            return ResponseEntity.status(HttpStatus.OK).body(analysis);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/project/url")
    public ResponseEntity<?> getAnalysisByProjectUrl(@RequestParam(name = "key") String projectUrl) {
        try {
            Collection<GithubAnalysis> analysis = githubAnalysisService.getAnalysisByProjectUrl(projectUrl);
            return ResponseEntity.status(HttpStatus.OK).body(analysis);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/project/latest/{projectId}")
    public ResponseEntity<?> getLatestAnalysisByProjectId(@PathVariable Long projectId) {
        try {
            GithubAnalysis analysis = githubAnalysisService.getLatestAnalysisByProjectId(projectId);
            return ResponseEntity.status(HttpStatus.OK).body(analysis);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            Map<String, String> response = Map.of("message", e.getMessage());

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
