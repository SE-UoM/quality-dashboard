package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
    Optional<Commit> findByProjectName(String projectName);

    default Optional<Commit> findById(String commitId){
        Long id = Long.parseLong(commitId);
        return findById(id);
    }
    Optional<Commit> findByHash(String hash);

    // Finds organization commits by year (use ORG_ID and YEAR)
    @Query(value = "SELECT c FROM Commit c WHERE c.project.organization.id = :orgId AND YEAR(c.commitDate) = :year")
    Collection<Commit> findOrgCommitsByYear(int year, Long orgId);

}
