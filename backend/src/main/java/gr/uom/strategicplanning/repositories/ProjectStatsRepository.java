package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.stats.ProjectStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatsRepository extends JpaRepository<ProjectStats, Long> {
}
