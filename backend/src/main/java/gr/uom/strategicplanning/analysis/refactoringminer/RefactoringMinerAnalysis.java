package gr.uom.strategicplanning.analysis.refactoringminer;

import gr.uom.strategicplanning.services.RefactoringMinerService;
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

    public RefactoringMinerAnalysis(String gitUrl, String branch, String projectName, RefactoringMinerService refactoringMinerService) {
        numberOfRefactorings = 0;
        this.refactoringMinerService = refactoringMinerService;
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
                        numberOfRefactorings++;

                        String refactoringType = ref.getRefactoringType().toString();
                        String refactoringName = ref.getName();
                        String refactoringDescription = ref.toString();
                        String refactoringClassesBefore = ref.getInvolvedClassesBeforeRefactoring().toString();
                        String refactoringClassesAfter = ref.getInvolvedClassesAfterRefactoring().toString();

                        refactoringMinerService.saveRefactoring(projectName, commitId, refactoringType, refactoringName, refactoringDescription, refactoringClassesBefore, refactoringClassesAfter);

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
