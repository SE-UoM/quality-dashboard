package uom.qualitydashboard.githubanalysisservice.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.githubanalysisservice.client.GithubApiClient;
import uom.qualitydashboard.githubanalysisservice.client.ProjectSubmissionMicroserviceClient;
import uom.qualitydashboard.githubanalysisservice.models.GithubAnalysis;
import uom.qualitydashboard.githubanalysisservice.models.SubmittedProjectDTO;
import uom.qualitydashboard.githubanalysisservice.repositories.GithubAnalysisRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GithubAnalysisService {
    private final GithubApiClient githubApiClient;
    private final ProjectSubmissionMicroserviceClient projectSubmissionMicroserviceClient;
    private final GithubAnalysisRepository githubAnalysisRepository;

    @Value("${github.token}")
    public String GITHUB_API_TOKEN;

    public GithubAnalysis startAnalysis(Long projectId) {
        String token = "Bearer " + GITHUB_API_TOKEN;

        Optional<SubmittedProjectDTO> submittedProject = projectSubmissionMicroserviceClient.getSubmittedProjectById(projectId);

        if (submittedProject.isEmpty()) throw new EntityNotFoundException("Project not found");

        // Start the analysis
        SubmittedProjectDTO project = submittedProject.get();
        String repoUrl = project.getRepoUrl();

        // Get repo name and owner from the URL
        String[] repoUrlParts = repoUrl.split("/");
        String owner = repoUrlParts[repoUrlParts.length - 2];
        String repo = repoUrlParts[repoUrlParts.length - 1];

        System.out.println("API:" + token);

        // Call the Github API to get the details of the repository
        Map<String, ?> repoDetails = githubApiClient.getRepoDetails(owner, repo, token);

        // Extract the details and save them to the database
        String name = (String) repoDetails.get("name");
        String fullName = (String) repoDetails.get("full_name");
        String description = (String) repoDetails.get("description");
        String url = project.getRepoUrl();
        String defaultBranch = (String) repoDetails.get("default_branch");
        Map<String, String> ownerDetails = (Map) repoDetails.get("owner");
        String ownerName = ownerDetails.get("login");

        int stars = (int) repoDetails.get("stargazers_count");
        int forks = (int) repoDetails.get("forks_count");
        int openIssues = (int) repoDetails.get("open_issues_count");
        Long organizationId = project.getOrganizationId();

        // Get the total number of commits
        int totalCommits = calculateTotalCommits(owner, repo);

        // Create a new GithubAnalysis object and save it to the database
        GithubAnalysis analysis = GithubAnalysis.builder()
                .projectId(projectId)
                .projectName(name)
                .projectFullName(fullName)
                .ownerName(ownerName)
                .projectDescription(description)
                .projectUrl(url)
                .defaultBranch(defaultBranch)
                .stars(stars)
                .forks(forks)
                .openIssues(openIssues)
                .totalCommits(totalCommits)
                .organizationId(organizationId)
                .build();

        return githubAnalysisRepository.save(analysis);
    }

    public Collection<GithubAnalysis> getAnalysisByProjectId(Long projectId) {
        return githubAnalysisRepository.findAllByProjectId(projectId);
    }

    public Collection<GithubAnalysis> getAnalysisByProjectName(String projectName) {
        Collection<GithubAnalysis> analysis = githubAnalysisRepository.findByProjectName(projectName);

        if (analysis.isEmpty()) throw new EntityNotFoundException("Analysis for project with name '" + projectName + "' not found");

        return analysis;
    }

    public Collection<GithubAnalysis> getAnalysisByProjectFullName(String projectFullName) {
        Collection<GithubAnalysis> analysis = githubAnalysisRepository.findByProjectFullName(projectFullName);

        if (analysis.isEmpty()) throw new EntityNotFoundException("Analysis for project with full name '" + projectFullName + "' not found");

        return analysis;
    }

    public Collection<GithubAnalysis> getAnalysisByProjectUrl(String projectUrl) {
        Collection<GithubAnalysis> analysis = githubAnalysisRepository.findByProjectUrl(projectUrl);

        if (analysis.isEmpty()) throw new EntityNotFoundException("Analysis for project with url '" + projectUrl + "' not found");

        return analysis;
    }

    public GithubAnalysis getLatestAnalysisByProjectId(Long projectId) {
        Optional<GithubAnalysis> analysis = githubAnalysisRepository.findTopByProjectIdOrderByLastAnalysisDateDesc(projectId);

        if (analysis.isEmpty()) throw new EntityNotFoundException("Analysis for project with id '" + projectId + "' not found");

        return analysis.get();
    }

    public Collection<GithubAnalysis> getAllGithubAnalyses() {
        return githubAnalysisRepository.findAll();
    }

    private int calculateTotalCommits(String owner, String repo) {
        String token = "Bearer " + GITHUB_API_TOKEN;
        Collection<?> contributors = githubApiClient.getRepoContributors(owner, repo, token);

        int totalCommits = 0;
        for (Object contributor : contributors) {
            Map<String, ?> contributorMap = (Map<String, ?>) contributor;
            totalCommits += (int) contributorMap.get("contributions");
        }

        return totalCommits;
    }
}
