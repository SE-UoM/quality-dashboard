package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.stats.CodeInspectorStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeInspectorStatsRepository extends JpaRepository<CodeInspectorStats, Long> {
    Optional<CodeInspectorStats> findByOrganizationAnalysisId(Long organizationAnalysisId);
}
