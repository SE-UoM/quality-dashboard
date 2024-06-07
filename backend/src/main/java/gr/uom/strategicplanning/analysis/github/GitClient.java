package gr.uom.strategicplanning.analysis.github;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The GitClient class provides utility methods to interact with Git repositories.
 */
public class GitClient {
    private GitClient() {
        // Private constructor to prevent instantiation
        throw new IllegalStateException("Utility class");
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
     * Fetches the commit SHAs of the commits in the repository associated with the project.
     *
     * @param project The Project object representing the project whose commit SHAs are to be fetched.
     * @return A list of commit SHAs
     */
    public static List<String> fetchCommitSHAsList(Project project) {
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

    /**
     * Checks out the commit with the specified SHA in the repository associated with the project.
     *
     * @param project The Project object representing the project whose commit is to be checked out.
     * @param commitSHA The SHA of the commit to be checked out
     */
    public static void checkoutCommit(Project project, String commitSHA) {
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
    public static void checkoutMaster(Project project) throws Exception {
        try {
            Git git = Git.open(new File("./repos/" + project.getName()));
            git.checkout().setName(project.getDefaultBranchName()).call();
            git.close();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the commit date of the commit associated with the project.
     *
     * @param clonedGit The Git object representing the cloned repository.
     * @return The date of the commit
     */
    public static String getShaOfClonedProject(Git clonedGit){
        Ref head = clonedGit.getRepository().getAllRefs().get("HEAD");
        return head.getObjectId().getName();
    }

    /**
     * Fetches the commit date of the commit associated with the project.
     *
     * @param project The Project object representing the project whose commit date is to be fetched.
     * @param commit The Commit object representing the commit whose date is to be fetched.
     * @return The date of the commit
     */
    public static Date fetchCommitDate(Project project, Commit commit) {
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
}
