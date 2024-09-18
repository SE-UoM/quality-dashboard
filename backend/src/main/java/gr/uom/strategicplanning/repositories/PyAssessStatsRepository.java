package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.stats.PyAssessStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PyAssessStatsRepository extends JpaRepository<PyAssessStats, Long> {
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM PyAssessStats p JOIN p.gitUrls g WHERE g = :gitUrl")
    Optional<PyAssessStats> existsInGitUrls(@Param("gitUrl") String gitUrl);
}
