package uom.qualitydashboard.githubanalysisservice.client;

import feign.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import uom.qualitydashboard.githubanalysisservice.services.GithubAnalysisService;

import java.util.Collection;
import java.util.Map;

@FeignClient(name = "github-api-client", url = "https://api.github.com")
public interface GithubApiClient {
    @GetMapping("/repos/{owner}/{repo}")
    Map<String, ?> getRepoDetails(@PathVariable String owner, @PathVariable String repo, @RequestHeader("Authorization") String token);

    @GetMapping("/repos/{owner}/{repo}/commits")
    Collection<?> getRepoCommits(@PathVariable String owner, @PathVariable String repo, @RequestHeader("Authorization") String token);

    @GetMapping("/repos/{owner}/{repo}/contributors")
    Collection<?> getRepoContributors(@PathVariable String owner, @PathVariable String repo, @RequestHeader("Authorization") String token);
}
