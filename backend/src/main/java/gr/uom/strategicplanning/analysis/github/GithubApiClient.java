package gr.uom.strategicplanning.analysis.github;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.squareup.okhttp.Response;
import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.models.Project;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * The GithubApiClient class extends the HttpClient class and provides methods to fetch project data from the GitHub API.
 */
public class GithubApiClient extends HttpClient {
    public RepositoryService repoService = new RepositoryService();
    public CommitService commitService = new CommitService();
    public Repository repository = new Repository();


    /**
     * Fetches project data from the GitHub API and populates the provided Project object with the retrieved information.
     *
     * @param project the Project object to populate with project data
     * @throws Exception if an I/O error occurs during the API request
     */
    public void fetchProjectData(Project project) throws Exception {

        String username = extractUsername(project.getRepoUrl());
        String repoName = extractRepoName(project.getRepoUrl());

        this.repository = repoService.getRepository(username, repoName);

        project.setName(repoName);
        project.setTotalCommits(this.captureTotalCommits());
        project.setForks(this.repository.getForks());
        project.setStars(this.getTotalStars());
    }

    /**
     * Retrieves the number of stargazers for a GitHub repository.
     *
     * @return the number of stargazers for the repository, or 0 if an error occurs
     * @throws Exception if an error occurs during the API request
     */

    public int getTotalStars() throws IOException {
        String repoName = this.repository.getName();
        String username = this.repository.getOwner().getLogin();

        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        Response response = this.sendGetRequest(url);

        // Convert response to JSONObject
        Map<String, Object> jsonMap = this.gson.fromJson(response.body().string(), Map.class);
        JSONObject jsonObject = new JSONObject(jsonMap);

        // Get total stars
        Number starsFloat = jsonObject.getAsNumber("stargazers_count");

        return starsFloat.intValue();
    }

    /**
     * Entry point of the GithubApiClient class for testing.
     *
     * @param args command-line arguments
     * @throws Exception if an error occurs during the API request
     */

    public static void main(String[] args) throws Exception {

        Project project1 = new Project();
        project1.setRepoUrl("https://github.com/StanGirard/quivr");

        GithubApiClient githubApiClient = new GithubApiClient();
        githubApiClient.fetchProjectData(project1);
    }

    /**
     * Extracts the username from a GitHub repository URL.
     *
     * @param repoUrl the URL of the GitHub repository
     * @return the username of the repository owner
     */
    private String extractUsername(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 2];
    }

    /**
     * Extracts the repository name from a GitHub repository URL.
     *
     * @param repoUrl the URL of the GitHub repository
     * @return the name of the repository
     */
    private String extractRepoName(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 1];
    }

    /**
     * Captures the total number of commits for the repository.
     *
     * @return the total number of commits for the repository
     * @throws Exception if an error occurs during the API request
     */
    private int captureTotalCommits() throws Exception {
        return this.commitService.getCommits(repository).size();
    }
}
