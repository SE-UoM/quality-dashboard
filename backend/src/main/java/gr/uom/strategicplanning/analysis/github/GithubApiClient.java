package gr.uom.strategicplanning.analysis.github;

import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.models.Project;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * The GithubApiClient class extends the HttpClient class and provides methods to fetch project data from the GitHub API.
 */
public class GithubApiClient extends HttpClient {
    public final int COMMITS_THRESHOLD = 50;

    public RepositoryService repoService = new RepositoryService();
    public CommitService commitService = new CommitService();
    public Repository repository = new Repository();
    public RestTemplate restTemplate = new RestTemplate();


    /**
     * Fetches project data from the GitHub API and populates the provided Project object with the retrieved information.
     *
     * @param project the Project object to populate with project data
     * @throws IOException if an I/O error occurs during the API request
     */
    public void fetchProjectData(Project project) throws Exception {

        String username = extractUsername(project.getRepoUrl());
        String repoName = extractRepoName(project.getRepoUrl());

        this.repository = repoService.getRepository(username, repoName);

        project.setTotalCommits(this.captureTotalCommits());
        project.setForks(this.repository.getForks());
        project.setStars(getStargazersCount(username, repoName));
    }

    public int getStargazersCount(String username, String repoName) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/stargazers", username, repoName);
        ResponseEntity response = this.restTemplate.getForEntity(url, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // TODO : check if this is the correct way to get the number of stargazers
            return response.getBody().toString().split("\n").length;
        } else {
            return 0;
        }
    }


    public static void main(String[] args) throws Exception {

        Project project1 = new Project();
        project1.setRepoUrl("https://github.com/StanGirard/quivr");

        GithubApiClient githubApiClient = new GithubApiClient();
        githubApiClient.fetchProjectData(project1);
    }

    private String extractUsername(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 2];
    }

    private String extractRepoName(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 1];
    }

    private int captureTotalCommits() throws Exception {
        return this.commitService.getCommits(repository).size();
    }
}
