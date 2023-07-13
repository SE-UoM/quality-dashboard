package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.CodeSmell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSmellRepository extends JpaRepository<CodeSmell, Long> {

}
