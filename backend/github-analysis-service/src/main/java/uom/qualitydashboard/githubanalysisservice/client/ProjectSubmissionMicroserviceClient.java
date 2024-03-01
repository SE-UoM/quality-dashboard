package uom.qualitydashboard.githubanalysisservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uom.qualitydashboard.githubanalysisservice.models.SubmittedProjectDTO;

import java.util.Optional;

@FeignClient(name = "project-submission-microservice", url = "http://localhost:8088/api/v1/projects/submissions")
public interface ProjectSubmissionMicroserviceClient {
    @GetMapping("/{projectId}")
    Optional<SubmittedProjectDTO> getSubmittedProjectById(@PathVariable Long projectId);
}
