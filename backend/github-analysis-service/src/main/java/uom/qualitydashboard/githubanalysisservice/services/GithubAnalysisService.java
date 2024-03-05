package uom.qualitydashboard.githubanalysisservice.services;

import feign.FeignException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.githubanalysisservice.client.DeveloperMicroservice;
import uom.qualitydashboard.githubanalysisservice.client.GithubApiClient;
import uom.qualitydashboard.githubanalysisservice.client.ProjectSubmissionMicroserviceClient;
import uom.qualitydashboard.githubanalysisservice.models.CreateDeveloperRequest;
import uom.qualitydashboard.githubanalysisservice.models.DeveloperDTO;
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
    private final DeveloperMicroservice developerMicroservice;

    @Value("${github.token}")
    public String GITHUB_API_TOKEN;

    public GithubAnalysis startAnalysis(Long projectId) {
        String token = "Bearer " + GITHUB_API_TOKEN;

        // Start the analysis
        SubmittedProjectDTO project = this.getSubmittedProjectById(projectId);
        String repoUrl = project.getRepoUrl();

        // Get repo name and owner from the URL
        Map<String, String> repoDetails = this.extractRepoDetails(repoUrl);
        String owner = repoDetails.get("owner");
        String repo = repoDetails.get("repo");

        GithubAnalysis analysis = this.extractGithubAnalysis(project, owner, repo, token);

        this.findRepositoryDevelopers(owner, repo, token);

        return githubAnalysisRepository.save(analysis);
    }

    private Map<String, String> extractRepoDetails(String repoUrl) {
        String[] repoUrlParts = repoUrl.split("/");
        String owner = repoUrlParts[repoUrlParts.length - 2];
        String repo = repoUrlParts[repoUrlParts.length - 1];

        return Map.of("owner", owner, "repo", repo);
    }

    private SubmittedProjectDTO getSubmittedProjectById(Long projectId) {
        Optional<SubmittedProjectDTO> project = projectSubmissionMicroserviceClient.getSubmittedProjectById(projectId);

        if (project.isEmpty()) throw new EntityNotFoundException("Project not found");

        return project.get();

    }

    private GithubAnalysis extractGithubAnalysis(SubmittedProjectDTO project, String owner, String repo, String token) {
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
        Long projectId = project.getId();

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

        return analysis;
    }

    private void findRepositoryDevelopers(String owner, String repo, String token) {
        // Now Find the project developers
        Collection<?> developers = githubApiClient.getRepoContributors(owner, repo, token);

        for (Object developer : developers) {
            Map<String, ?> developerMap = (Map<String, ?>) developer;

            Optional<DeveloperDTO> developerResponse = tryToCallCreateDevAPI(developerMap);
        }
    }

    private Optional<DeveloperDTO> tryToCallCreateDevAPI(Map<String, ?> developerMap) {
        // Set Up Data
        String developerName = (String) developerMap.get("login");
        String avatarUrl = (String) developerMap.get("avatar_url");
        String profileUrl = (String) developerMap.get("html_url");

        CreateDeveloperRequest request = CreateDeveloperRequest.builder()
                .name(developerName)
                .githubProfileURL(profileUrl)
                .imageURI(avatarUrl)
                .build();

        // Try to create the developer
        try {
            DeveloperDTO developerResponse = developerMicroservice.createDeveloper(request);

            return Optional.of(developerResponse);
        }
        catch (FeignException.Conflict e) {
            // If the developer already exists, do nothing
        }

        return Optional.empty();
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
