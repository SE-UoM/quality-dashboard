package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.stats.PyAssessStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PyAssessStatsRepository extends JpaRepository<PyAssessStats, Long> {
    Optional<PyAssessStats> findByOrganizationAnalysisId(Long organizationAnalysisId);
}
