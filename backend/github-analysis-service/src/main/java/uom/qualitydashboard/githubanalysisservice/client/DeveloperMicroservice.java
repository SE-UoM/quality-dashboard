package uom.qualitydashboard.githubanalysisservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uom.qualitydashboard.githubanalysisservice.models.CreateDeveloperRequest;
import uom.qualitydashboard.githubanalysisservice.models.DeveloperDTO;

import java.util.Optional;

@FeignClient(name = "developer-service", url = "http://localhost:8088/api/v1/developers")
public interface DeveloperMicroservice {
    @GetMapping("/{developerId}")
    Optional<DeveloperDTO> getDeveloperById(@PathVariable Long developerId);

    @PostMapping("/create")
    Optional<DeveloperDTO> createDeveloper(@RequestBody CreateDeveloperRequest request);

    @GetMapping("/github-profile-url")
    Optional<DeveloperDTO> getDeveloperByGithubProfileURL(@RequestParam(name = "key") String githubProfileURL);
}
