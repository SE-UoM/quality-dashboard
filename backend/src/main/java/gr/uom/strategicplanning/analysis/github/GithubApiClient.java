package gr.uom.strategicplanning.analysis.github;

import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * The GithubApiClient class extends the HttpClient class and provides methods to fetch project data from the GitHub API.
 */
public class GithubApiClient extends HttpClient {
    private RestTemplate restTemplate = new RestTemplate();
    private String githubToken;

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

        project.setName(repoName);
        project.setTotalCommits(this.captureTotalCommits(username, repoName));
        project.setForks(this.getTotalForks(username, repoName));
        project.setStars(this.getTotalStars(username, repoName));
    }

    public Map<String, Integer> languageResponse(Project project) {
        String[] split = project.getRepoUrl().split("/");
        String owner = split[split.length - 2];
        String name = split[split.length - 1];

        String url = "https://api.github.com/repos/" + owner + "/" + name + "/languages";

        return sendGithubRequest(url);
    }

    private Map sendGithubRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);

        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(url))
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<Map> response = restTemplate.exchange(requestEntity, Map.class);

        return response.getBody();
    }

    /**
     * Retrieves the number of stargazers for a GitHub repository.
     *
     * @return the number of stargazers for the repository, or 0 if an error occurs
     * @throws Exception if an error occurs during the API request
     */

    public int getTotalStars(String username, String repoName) throws IOException {
        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        Map<String, Object> jsonMap = sendGithubRequest(url);

        Number starsFloat = (Number) jsonMap.get("stargazers_count");

        return starsFloat != null ? starsFloat.intValue() : 0;
    }

    public int getTotalForks(String username, String repoName) throws IOException {

        String url = String.format("https://api.github.com/repos/%s/%s", username, repoName);
        Map<String, Object> jsonMap = sendGithubRequest(url);

        Number forksFloat = (Number) jsonMap.get("forks_count");

        return forksFloat != null ? forksFloat.intValue() : 0;
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
    private int captureTotalCommits(String username, String repoName) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits", username, repoName);
        Map<String, Object> jsonMap = sendGithubRequest(url);

        return jsonMap.size();
    }

    /**
     * Deletes the cloned repository associated with the project.
     *
     * @param project The Project object representing the project whose repository is to be deleted.
     * @throws Exception if an error occurs during the deletion process
     */
    public static void deleteRepository(Project project) throws Exception {
        String repoName = project.getName();
        File file = new File(System.getProperty("user" + ".dir") + "/repos/" + repoName);
        file.delete();
    }

    /**
     * Clones the repository associated with the project to a local directory.
     *
     * @param project The Project object representing the project whose repository is to be cloned.
     * @throws Exception if an error occurs during the cloning process
     */
    public static void cloneRepository(Project project) throws Exception {
        Git.cloneRepository()
                .setURI(project.getRepoUrl())
                .setDirectory(new File(System.getProperty("user" + ".dir") + "/repos" + "/" + project.getName()))
                .call();
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

    public Date fetchCommitDate(Project project, Commit commit) {
        try {
            Git git = Git.open(new File("./repos/" + project.getName()));
            RevCommit jgitCommit = git.getRepository().parseCommit(ObjectId.fromString(commit.getHash()));
            return jgitCommit.getAuthorIdent().getWhen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String fetchDeveloperName(Project project, Commit commit) throws IOException {
        Git git = Git.open(new File("./repos/" + project.getName()));
        RevCommit jgitCommit = git.getRepository().parseCommit(ObjectId.fromString(commit.getHash()));
        return jgitCommit.getAuthorIdent().getName();
    }
}
