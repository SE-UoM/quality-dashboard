package uom.qualitydashboard.githubanalysisservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Map;

@FeignClient(name = "github-api-client", url = "https://api.github.com")
public interface GithubApiClient {
    @GetMapping("/repos/{owner}/{repo}")
    Map<String, ?> getRepoDetails(@PathVariable String owner, @PathVariable String repo);

    @GetMapping("/repos/{owner}/{repo}/commits")
    Collection<?> getRepoCommits(@PathVariable String owner, @PathVariable String repo);

    @GetMapping("/repos/{owner}/{repo}/contributors")
    Collection<?> getRepoContributors(@PathVariable String owner, @PathVariable String repo);
}
