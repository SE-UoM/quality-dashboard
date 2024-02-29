package uom.qualitydashboard.projectsubmissionservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.projectsubmissionservice.client.UserMicroserviceClient;
import uom.qualitydashboard.projectsubmissionservice.models.ProjectSubmissionRequest;
import uom.qualitydashboard.projectsubmissionservice.models.SubmittedProject;
import uom.qualitydashboard.projectsubmissionservice.models.User;
import uom.qualitydashboard.projectsubmissionservice.repos.SubmittedProjectRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmittedProjectService {
    private final SubmittedProjectRepository submittedProjectRepository;
    private final UserMicroserviceClient userMicroserviceClient;

    public SubmittedProject submitProject(ProjectSubmissionRequest projectSubmissionRequest) {
        String repoUrl = projectSubmissionRequest.getRepoUrl();
        Long userId = projectSubmissionRequest.getUserId();

        if (!urlIsValidGithubRepo(repoUrl)) throw new IllegalArgumentException("Invalid Github repository URL");

        Optional<User> foundUser = userMicroserviceClient.getUserById(userId);

        if (foundUser.isEmpty()) throw new IllegalArgumentException("User not found");

        User user = foundUser.get();
        Long organizationId = user.getOrganizationId();
        Date timestamp = new Date();

        SubmittedProject submittedProject = SubmittedProject.builder()
                .repoUrl(repoUrl)
                .userId(userId)
                .lastAnalysisDate(null)
                .submissionDate(timestamp)
                .organizationId(organizationId)
                .build();

        return submittedProjectRepository.save(submittedProject);
    }

    public Collection<SubmittedProject> getAllSubmissions() {
        return submittedProjectRepository.findAll();
    }

    public Optional<SubmittedProject> getSubmissionById(Long id) {
        return submittedProjectRepository.findById(id);
    }

    public Collection<SubmittedProject> getSubmissionsByUserId(Long userId) {
        return submittedProjectRepository.findByUserId(userId);
    }

    public Collection<SubmittedProject> getSubmissionsByOrganizationId(Long organizationId) {
        return submittedProjectRepository.findByOrganizationId(organizationId);
    }

    private boolean urlIsValidGithubRepo(String repoUrl) {
        // Github repo Regex
        String githubRepoRegex = "^(https?://)?(www\\.)?github\\.com/.+/.+$";

        if (!repoUrl.matches(githubRepoRegex)) return false;

        // TODO: Check if the repo exists using Github API and not just validate the URL format

        return true;
    }
}
