package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
    Optional<Commit> findByProjectName(String projectName);
}
