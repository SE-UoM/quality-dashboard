package gr.uom.strategicplanning.analysis.github;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The GithubApiClient class extends the HttpClient class and provides methods to fetch project data from the GitHub API.
 */
public class GithubApiClient extends HttpClient {
    private String githubToken;
    public static final String DEFAULT_AVATAR_URL = "https://api.github.com";

    public GithubApiClient(String token) {
        super();
        this.githubToken = token;
    }

    /**
     * Fetches project data from the GitHub API and populates the provided Project object with the retrieved information.
     *
     * @param project the Project object to populate with project data
     * @throws Exception if an I/O error occurs during the API request
     */

    public void fetchProjectData(Project project) throws Exception {

        String username = extractUsername(project.getRepoUrl());
        String repoName = extractRepoName(project.getRepoUrl());
        Map repoData = callReposAPI(username, repoName);

        String description = (String) repoData.get("description");
        String defaultBranch = (String) repoData.get("default_branch");
        Double totalForks = (Double) repoData.get("forks_count");
        Double totalStars = (Double) repoData.get("stargazers_count");

        int totalCommits = getTotalCommits(project);

        project.setName(repoName);
        project.setProjectDescription(description);
        project.setOwnerName(username);
        project.setTotalCommits(totalCommits);
        project.setForks(totalForks.intValue());
        project.setStars(totalStars.intValue());
        project.setDefaultBranchName(defaultBranch);
    }

    private Map<String, Object> callReposAPI(String username, String repoName) {
        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        try {
            Response response = sendGithubRequest(url);

            if (!response.isSuccessful())
                throw new IOException("Failed to fetch repository from GitHub API");

            return gson.fromJson(response.body().string(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Response sendGithubRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + githubToken)
                .build();

        return client.newCall(request).execute();
    }

    public boolean repoFoundByGithubAPI(String repoUrl) {
        String username = extractUsername(repoUrl);
        String repoName = extractRepoName(repoUrl);
        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        try {
            Response response = sendGithubRequest(url);

            if (response.isSuccessful())
                return true;

            if (response.code() != HttpStatus.NOT_FOUND.value())
                throw new IOException("Failed to fetch repository from GitHub API");

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalCommits(Project project) throws IOException {
        String username = extractUsername(project.getRepoUrl());
        String repoName = extractRepoName(project.getRepoUrl());

        // This makes sure we get one commit per page, and only the first page
        String url = String.format("https://api.github.com/repos/%s/%s/commits?per_page=1&page=1", username, repoName);

        // To find the total number of commits, we need to check the "Link" header.
        Response response = sendGithubRequest(url);
        String linkHeader = response.header("Link");

        if (linkHeader == null) return 1;

        return findLastPageNumber(linkHeader);
    }

    /**
     * Finds the last page number from the "Link" header of a GitHub API response.
     * The logic is that since each page contains 1 commit, the last page number is the total number of commits.
     *
     * @param linkHeader the "Link" header of a GitHub API response
     * @return the last page number
     */
    private int findLastPageNumber(String linkHeader) {
        String[] links = linkHeader.split(",");
        for (String link : links) {
            if (link.contains("rel=\"last\"")) {
                String[] parts = link.split("&page=");

                // Extract the last page number
                String lastPage = parts[1].split(">")[0];

                // Convert the last page number to an integer
                return Integer.parseInt(lastPage);
            }
        }
        return 1;
    }

    /**
     * Extracts the username from a GitHub repository URL.
     *
     * @param repoUrl the URL of the GitHub repository
     * @return the username of the repository owner
     */
    private static String extractUsername(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 2];
    }

    /**
     * Extracts the repository name from a GitHub repository URL.
     *
     * @param repoUrl the URL of the GitHub repository
     * @return the name of the repository
     */
    private static String extractRepoName(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 1];
    }

    public String fetchDeveloperNameFromCommit(Project project, Commit commit) throws IOException {
        String commitSHA = commit.getHash();

        String repoURL = project.getRepoUrl();
        String owner = extractUsername(repoURL);
        String repoName = extractRepoName(repoURL);

        String commitsAPI = String.format("https://api.github.com/repos/%s/%s/commits/%s", owner, repoName, commitSHA);

        Response commitJSON = sendGithubRequest(commitsAPI);
        JSONObject jsonObject = new JSONObject(commitJSON.body().string());

        try {
            JSONObject author = jsonObject.getJSONObject("author");
            String name = author.getString("login");
            return name;
        }
        catch (JSONException e) {
            return null;
        }

    }

    public String fetchGithubURL(String name) throws IOException {
        String usersAPI = String.format("https://api.github.com/users/%s", name);

        Response response = sendGithubRequest(usersAPI);
        JSONObject jsonObject = new JSONObject(response.body().string());
        String githubURL = jsonObject.getString("html_url");

        if (githubURL == null) return "";

        return githubURL;
    }

    public String fetchAvatarUrl(String name) throws IOException {
        String usersAPI = String.format("https://api.github.com/users/%s", name);

        Response response = sendGithubRequest(usersAPI);
        JSONObject jsonObject = new JSONObject(response.body().string());

        String avatarUrl = jsonObject.getString("avatar_url");

        if (avatarUrl == null) return DEFAULT_AVATAR_URL;

        return avatarUrl;
    }
}
