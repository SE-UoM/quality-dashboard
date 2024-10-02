package gr.uom.strategicplanning.analysis.refactoringminer;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.RefactoringModel;
import gr.uom.strategicplanning.services.CommitService;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefactoringMinerAnalysis {
    private int numberOfRefactorings;
    private CommitService commitService;

    public RefactoringMinerAnalysis(CommitService commitService) {
        numberOfRefactorings = 0;
        this.commitService = commitService;
    }

    public Map<String, Object> getTotalNumberOfRefactorings(String gitUrl, String projectName, String branch) throws Exception {
        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

        Repository repo = gitService.cloneIfNotExists(System.getProperty("user.dir") + System.getProperty("file.separator") + "repos" + System.getProperty("file.separator") + projectName, gitUrl);

        Map<String, Object> results = new HashMap<>();
        List<RefactoringModel> refactoringModels = new ArrayList<>();

        try {
            miner.detectAll(repo, branch, new RefactoringHandler() {
                @Override
                public void handle(String commitId, List<Refactoring> refactorings) {
                    for (Refactoring ref : refactorings) {
                        numberOfRefactorings++;
                        RefactoringModel refactoringModel = new RefactoringModel(ref);
                        Commit commit = commitService.getCommitByCommitHashOrCreate(commitId);
                        refactoringModel.setCommit(commit);
                        refactoringModels.add(refactoringModel);
                    }
                }
            });

            repo.close();

            results.put("refactorings", refactoringModels);
            results.put("totalRefactorings", numberOfRefactorings);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }

}
