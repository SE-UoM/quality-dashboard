package gr.uom.strategicplanning.analysis.github;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.squareup.okhttp.Response;
import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.models.Developer;
import gr.uom.strategicplanning.models.Project;

import java.io.IOException;
import java.util.Map;

/**
 * The GithubApiClient class extends the HttpClient class and provides methods to fetch project data from the GitHub API.
 */
public class GithubApiClient extends HttpClient {
    private final String GITHUB_API_URL = "https://api.github.com/repos";
    public static final int COMMITS_THRESHOLD = 50;

    private String projectSpecificUrl;

    private JSONObject jsonObject;

    /**
     * Fetches project data from the GitHub API and populates the provided Project object with the retrieved information.
     *
     * @param project the Project object to populate with project data
     * @throws IOException if an I/O error occurs during the API request
     */
    public void fetchProjectData(Project project) throws IOException {
        String repoUrl = project.getRepoUrl();

        String username = extractUsername(repoUrl);
        String repoName = extractRepoName(repoUrl);
        this.projectSpecificUrl = buildApiUrl(username, repoName);

        System.out.println("Fetching data for " + repoUrl + "...");
        Response response = sendGetRequest(this.projectSpecificUrl);

        String responseBodyString = response.body().string();

        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(responseBodyString, Map.class);

        this.jsonObject = new JSONObject(map);
    }

    public static void main(String[] args) throws IOException {

        Project project1 = new Project();
        project1.setRepoUrl("https://github.com/StanGirard/quivr");

        GithubApiClient client = new GithubApiClient();
        client.fetchProjectData(project1);

        String projectName = client.getProjectName();
        float projectForks = client.getProjectForks();
        Developer projectDeveloper = client.getProjectDeveloper();

        client.getProjectCommitsNumber();

        System.out.println(projectName);
        System.out.println(projectForks);
        System.out.println(projectDeveloper);
    }

    public void getProjectCommitsNumber() throws IOException {
        // implement commits number
        System.out.println("Implement commits number");
        System.out.println("Not Implemented Yet");
    }

    public String getProjectName() {
        return jsonObject.get("name").toString();
    }

    public float getProjectForks() {
        return jsonObject.getAsNumber("forks_count").floatValue();
    }

    public Developer getProjectDeveloper() {
        Developer developer = new Developer();

        System.out.println(jsonObject.get("owner"));

        LinkedTreeMap map = (LinkedTreeMap) jsonObject.get("owner");

        String devName = map.get("login").toString();
        String devUrl = map.get("html_url").toString();

        developer.setName(devName);
        developer.setGithubUrl(devUrl);

        return developer;
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
