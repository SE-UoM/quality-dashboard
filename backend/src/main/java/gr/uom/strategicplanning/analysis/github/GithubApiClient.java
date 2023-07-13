package gr.uom.strategicplanning.logic.github;

import gr.uom.strategicplanning.logic.HttpClient;
import gr.uom.strategicplanning.models.Language;
import gr.uom.strategicplanning.models.Project;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * The GithubApiClient class extends the HttpClient class and provides methods to fetch project data from the GitHub API.
 */
public class GithubApiClient extends HttpClient {
    private final String GITHUB_API_URL = "https://api.github.com/repos";

    /**
     * Fetches project data from the GitHub API and populates the provided Project object with the retrieved information.
     *
     * @param project the Project object to populate with project data
     * @throws IOException if an I/O error occurs during the API request
     */
    public Map<String, String> fetchProjectData(Project project) throws IOException {
        String repoUrl = project.getRepoUrl();

        String username = extractUsername(repoUrl);
        String repoName = extractRepoName(repoUrl);
        String apiUrl = buildApiUrl(username, repoName);

        Map<String, String> projectData = super.sendGetRequest(apiUrl);

        return projectData;
    }

    public static void main(String[] args) throws IOException {
        // Github only allows 60 requests per hour for unauthenticated users.

        Project project1 = new Project();
        project1.setRepoUrl("https://github.com/StanGirard/quivr");

        Project project2 = new Project();
        project2.setRepoUrl("https://github.com/StanGirard/quivr");

        Project project3 = new Project();
        project3.setRepoUrl("https://github.com/StanGirard/quivr");

        // Fetch data for each project
        Arrays.asList(project1, project2, project3).parallelStream().forEach(p -> {
            try {
                GithubApiClient githubApiClient = new GithubApiClient();
                githubApiClient.fetchProjectData(p);

                // Log the project data
                System.out.println(p + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private String extractUsername(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 2];
    }

    private String extractRepoName(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 1];
    }

    private String buildApiUrl(String username, String repoName) {
        return GITHUB_API_URL + "/" + username + "/" + repoName;
    }
}