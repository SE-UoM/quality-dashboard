package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
}
