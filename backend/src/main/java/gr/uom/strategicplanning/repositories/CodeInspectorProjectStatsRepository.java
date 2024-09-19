package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.external.CodeInspectorProjectStats;
import gr.uom.strategicplanning.models.stats.CodeInspectorStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeInspectorProjectStatsRepository extends JpaRepository<CodeInspectorProjectStats, Long> {
    Optional<CodeInspectorProjectStats> findByProjectUrl(String gitUrl);
    Optional<CodeInspectorProjectStats> findByProjectId(Long projectId);
    Optional<CodeInspectorStats> findStatsByProjectUrl(String projectUrl);
}
