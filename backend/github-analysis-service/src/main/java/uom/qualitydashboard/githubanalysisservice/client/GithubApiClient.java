package uom.qualitydashboard.githubanalysisservice.client;

import feign.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Map;

@FeignClient(name = "github-api-client", url = "https://api.github.com")
public interface GithubApiClient {
    @Value("${github.api.token}")
    String GITHUB_API_TOKEN = "";

    @GetMapping("/repos/{owner}/{repo}")
    @Headers("Authorization: Bearer " + GITHUB_API_TOKEN)
    Map<String, ?> getRepoDetails(@PathVariable String owner, @PathVariable String repo);

    @GetMapping("/repos/{owner}/{repo}/commits")
    @Headers("Authorization: Bearer " + GITHUB_API_TOKEN)
    Collection<?> getRepoCommits(@PathVariable String owner, @PathVariable String repo);

    @GetMapping("/repos/{owner}/{repo}/contributors")
    @Headers("Authorization: Bearer " + GITHUB_API_TOKEN)
    Collection<?> getRepoContributors(@PathVariable String owner, @PathVariable String repo);
}
