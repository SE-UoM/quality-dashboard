package gr.uom.strategicplanning.analysis.refactoringminer;

import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import java.util.List;

public class RefactoringMinerAnalysis {

    private int numberOfRefactorings;
    private String gitUrl;
    private String branch;
    private String projectName;

    public RefactoringMinerAnalysis(String gitUrl, String branch, String projectName){
        numberOfRefactorings = 0;
        this.gitUrl = gitUrl;
        this.branch = branch;
        this.projectName = projectName;
    }

    public int getTotalNumberOfRefactorings() throws Exception {
        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

        Repository repo = gitService.cloneIfNotExists(System.getProperty("user.dir") + System.getProperty("file.separator") + "repos" + System.getProperty("file.separator") + projectName, gitUrl);

        try {
            miner.detectAll(repo, branch, new RefactoringHandler() {
                @Override
                public void handle(String commitId, List<Refactoring> refactorings) {
                    for (Refactoring ref : refactorings) {
                        numberOfRefactorings ++;
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return numberOfRefactorings;
    }

}
