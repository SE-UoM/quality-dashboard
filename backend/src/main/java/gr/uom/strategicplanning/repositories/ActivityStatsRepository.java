package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.stats.ActivityStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityStatsRepository extends JpaRepository<ActivityStats, Long> {

}
