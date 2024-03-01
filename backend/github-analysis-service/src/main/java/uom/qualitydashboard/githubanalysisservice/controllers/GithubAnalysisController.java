package uom.qualitydashboard.githubanalysisservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.qualitydashboard.githubanalysisservice.client.GithubApiClient;
import uom.qualitydashboard.githubanalysisservice.client.ProjectSubmissionMicroserviceClient;
import uom.qualitydashboard.githubanalysisservice.models.GithubAnalysis;
import uom.qualitydashboard.githubanalysisservice.models.SubmittedProjectDTO;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/github-analysis")
public class GithubAnalysisController {
    private final ProjectSubmissionMicroserviceClient projectSubmissionMicroserviceClient;
    private final GithubApiClient githubApiClient;

    @PostMapping("/start")
    public ResponseEntity<?> startAnalysis(@RequestParam(name = "projectId") Long projectId) {

        Optional<SubmittedProjectDTO> submittedProject = projectSubmissionMicroserviceClient.getSubmittedProjectById(projectId);

        if (submittedProject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }

        // Start the analysis
        SubmittedProjectDTO project = submittedProject.get();
        String repoUrl = project.getRepoUrl();

        // Get repo name and owner from the URL
        String[] repoUrlParts = repoUrl.split("/");
        String owner = repoUrlParts[repoUrlParts.length - 2];
        String repo = repoUrlParts[repoUrlParts.length - 1];

        // Get the repo details from the github api
        Map<String, ?> repoDetails = githubApiClient.getRepoDetails(owner, repo);

        // Extract the details and save them to the database
        String name = (String) repoDetails.get("name");
        String fullName = (String) repoDetails.get("full_name");
        String description = (String) repoDetails.get("description");
        String url = project.getRepoUrl();
        String defaultBranch = (String) repoDetails.get("default_branch");
        int stars = (int) repoDetails.get("stargazers_count");
        int forks = (int) repoDetails.get("forks_count");
        int openIssues = (int) repoDetails.get("open_issues_count");

        // Get the total number of commits
        int totalCommits = calculateTotalCommits(owner, repo);

        // Create a new GithubAnalysis object and save it to the database
        GithubAnalysis analysis = GithubAnalysis.builder()
                .projectId(projectId)
                .projectName(name)
                .projectFullName(fullName)
                .projectDescription(description)
                .projectUrl(url)
                .defaultBranch(defaultBranch)
                .stars(stars)
                .forks(forks)
                .openIssues(openIssues)
                .totalCommits(totalCommits)
                .build();

        return ResponseEntity.ok(analysis);
    }

    private int calculateTotalCommits(String owner, String repo) {
        Collection<?> contributors = githubApiClient.getRepoContributors(owner, repo);

        int totalCommits = 0;
        for (Object contributor : contributors) {
            Map<String, ?> contributorMap = (Map<String, ?>) contributor;
            totalCommits += (int) contributorMap.get("contributions");
        }

        return totalCommits;
    }
}
