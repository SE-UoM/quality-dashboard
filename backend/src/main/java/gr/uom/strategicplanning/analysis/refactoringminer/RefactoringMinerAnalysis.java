package gr.uom.strategicplanning.analysis.refactoringminer;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.RefactoringModel;
import gr.uom.strategicplanning.services.CommitService;
import gr.uom.strategicplanning.services.RefactoringMinerService;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RefactoringMinerAnalysis {

    private int numberOfRefactorings;
    private String gitUrl;
    private String branch;
    private String projectName;
    private RefactoringMinerService refactoringMinerService;
    private CommitService commitService;

    public RefactoringMinerAnalysis(String gitUrl, String branch, String projectName, RefactoringMinerService refactoringMinerService, CommitService commitService) {
        numberOfRefactorings = 0;
        this.refactoringMinerService = refactoringMinerService;
        this.gitUrl = gitUrl;
        this.branch = branch;
        this.projectName = projectName;
        this.commitService = commitService;
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
                        numberOfRefactorings++;
                        Commit commit = commitService.getCommitByCommitId(commitId);
                        RefactoringModel refactoringModel = new RefactoringModel(ref);
                        refactoringMinerService.saveRefactoring(refactoringModel);
                        commit.getRefactoringModels().add(refactoringModel);
                    }
                }
            });

            repo.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return numberOfRefactorings;
    }

}
