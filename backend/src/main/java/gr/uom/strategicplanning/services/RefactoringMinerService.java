package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.RefactoringModel;
import gr.uom.strategicplanning.repositories.RefactoringModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RefactoringMinerService {

    @Autowired
    private RefactoringModelRepository refactoringModelRepository;

    public void saveRefactoring(RefactoringModel ref) {
        refactoringModelRepository.save(ref);
    }
}
