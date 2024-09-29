package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.RefactoringModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefactoringModelRepository extends JpaRepository<RefactoringModel, Long> {

}
