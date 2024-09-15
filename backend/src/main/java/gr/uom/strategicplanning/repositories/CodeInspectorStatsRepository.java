package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.stats.CodeInspectorStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeInspectorStatsRepository extends JpaRepository<CodeInspectorStats, Long> {
}
