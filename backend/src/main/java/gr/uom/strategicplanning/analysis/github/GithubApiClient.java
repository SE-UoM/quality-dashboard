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
        String defaultBranch = getDefaultBranchName(project);
        String projectDescription = getProjectDescFromGithub(project.getRepoUrl());

        project.setName(repoName);
        project.setProjectDescription(projectDescription);
        project.setOwnerName(username);
        project.setTotalCommits(captureTotalCommits(username, repoName));
        project.setForks(getTotalForks(username, repoName));
        project.setStars(this.getTotalStars(username, repoName));
        project.setDefaultBranchName(defaultBranch);
    }

    private String getProjectDescFromGithub(String repoUrl) {
        String username = extractUsername(repoUrl);
        String repoName = extractRepoName(repoUrl);
        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        try {
            Response response = sendGithubRequest(url);

            if (response.isSuccessful()) {
                Map<String, Object> jsonMap = gson.fromJson(response.body().string(), Map.class);
                return (String) jsonMap.get("description");
            } else {
                throw new IOException("Failed to fetch repository from GitHub API");
            }
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

    public boolean repoHasLessThatThresholdCommits(Project project, int thresholdCommits) {
        String username = extractUsername(project.getRepoUrl());
        String repoName = extractRepoName(project.getRepoUrl());

        String url = String.format("https://api.github.com/repos/%s/%s/commits?per_page=100", username, repoName);

        try {
            Response response = sendGithubRequest(url);

            if (response.isSuccessful()) {
                List<Map<String, Object>> commitsList = gson.fromJson(response.body().string(), List.class);
                return commitsList.size() < thresholdCommits;
            } else {
                throw new IOException("Failed to fetch commits from GitHub API");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the number of stargazers for a GitHub repository.
     *
     * @return the number of stargazers for the repository, or 0 if an error occurs
     * @throws Exception if an error occurs during the API request
     */

    public int getTotalStars(String username, String repoName) throws IOException {
        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        Response response = sendGithubRequest(url);

        if (response.isSuccessful()) {
            Map<String, Object> jsonMap = gson.fromJson(response.body().string(), Map.class);
            Number starsFloat = (Number) jsonMap.get("stargazers_count");
            return starsFloat != null ? starsFloat.intValue() : 0;
        } else {
            throw new IOException("Failed to fetch total stars from GitHub API");
        }
    }

    public int getTotalForks(String username, String repoName) throws IOException {
        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        Response response = sendGithubRequest(url);

        if (response.isSuccessful()) {
            Map<String, Object> jsonMap = gson.fromJson(response.body().string(), Map.class);
            Number forksFloat = (Number) jsonMap.get("forks_count");
            return forksFloat != null ? forksFloat.intValue() : 0;
        } else {
            throw new IOException("Failed to fetch total forks from GitHub API");
        }
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
    private int captureTotalCommits(String username, String repoName) throws IOException {
        String url = String.format("https://api.github.com/repos/%s/%s/commits", username, repoName);
        Response response = sendGithubRequest(url);

        if (response.isSuccessful()) {
            List<Map<String, Object>> commitsList = gson.fromJson(response.body().string(), List.class);
            return commitsList.size();
        } else {
            throw new IOException("Failed to fetch commits from GitHub API");
        }
    }

    /**
     * Deletes the cloned repository associated with the project.
     *
     * @param project The Project object representing the project whose repository is to be deleted.
     * @throws Exception if an error occurs during the deletion process
     */
    public static void deleteRepository(Project project) throws Exception {
        String repoName = project.getName();
        String pathToRepo = System.getProperty("user.dir") + System.getProperty("file.separator") + "repos" + System.getProperty("file.separator") + repoName;
        Path path = Paths.get(pathToRepo);

        org.eclipse.jgit.util.FileUtils.delete(path.toFile(), org.eclipse.jgit.util.FileUtils.RECURSIVE);

        System.out.println("Deleted repository: " + repoName);
    }

    /**
     * Clones the repository associated with the project to a local directory.
     *
     * @param project The Project object representing the project whose repository is to be cloned.
     * @throws Exception if an error occurs during the cloning process
     */
    public static Git cloneRepository(Project project) throws Exception {
        System.out.println("Cloning repository: " + project.getName());
        System.out.println("User dir: " + System.getProperty("user.dir"));
        String pathToRepo = System.getProperty("user.dir") + System.getProperty("file.separator") + "repos" + System.getProperty("file.separator") + project.getName();

        Git git = Git.cloneRepository()
                .setURI(project.getRepoUrl())
                .setDirectory(new File(pathToRepo))
                .call();

        System.out.println("Cloned repository: " + project.getName());
        return git;
    }

    /**
     * Get the default branch name after cloning project
     *
     * @param project
     * @return the default branch name
     */
    public String getDefaultBranchName(Project project){
        String repoUrl = project.getRepoUrl();
        String username = extractUsername(repoUrl);
        String repoName = extractRepoName(repoUrl);
        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        try {
            Response response = new GithubApiClient(githubToken).sendGithubRequest(url);
            if (response.isSuccessful()) {
                Map<String, Object> jsonMap = gson.fromJson(response.body().string(), Map.class);
                return (String) jsonMap.get("default_branch");
            } else {
                throw new IOException("Failed to fetch default branch from GitHub API");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> fetchCommitSHA(Project project) {
        List<String> commitSHAList = new ArrayList<>();

        try {
            Git git = Git.open(new File("./repos/" + project.getName()));
            Iterable<RevCommit> commits = git.log().all().call();

            for (RevCommit commit : commits) {
                commitSHAList.add(commit.getId().getName());
            }

            git.close(); // Close the Git repository
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }

        return commitSHAList;
    }

    public void checkoutCommit(Project project, String commitSHA) {
        try {
            Git git = Git.open(new File("./repos/" + project.getName()));

            ObjectId commitId = git.getRepository().resolve(commitSHA); // Get the ObjectId of the commit

            if (commitId != null) {
                CheckoutCommand checkoutCommand = git.checkout();
                checkoutCommand.setName(commitId.getName()); //
                checkoutCommand.call(); //
            } else {
                System.out.println("Commit not found: " + commitSHA);
            }

            git.close(); //
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks out the master branch with the changes of the latest commit.
     *
     * @param project The Project object representing the project whose repository is to be checked out.
     * @throws Exception if an error occurs during the checkout process
     */
    public void checkoutMaster(Project project) throws Exception {
        try {
            Git git = Git.open(new File("./repos/" + project.getName()));
            git.checkout().setName(project.getDefaultBranchName()).call();
            git.close();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    public Date fetchCommitDate(Project project, Commit commit) {
        try {
            Git git = Git.open(new File("./repos/" + project.getName()));
            RevCommit jgitCommit = git.getRepository().parseCommit(ObjectId.fromString(commit.getHash()));
            git.close();
            return jgitCommit.getAuthorIdent().getWhen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
