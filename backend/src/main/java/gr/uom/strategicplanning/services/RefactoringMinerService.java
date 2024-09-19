package gr.uom.strategicplanning.services;

import org.springframework.stereotype.Service;

@Service
public class RefactoringMinerService {


    public void saveRefactoring(String projectName, String commitId, String refactoringType, String refactoringName, String refactoringDescription, String refactoringClassesBefore, String refactoringClassesAfter) {
        // Save refactoring to database
    }
}
