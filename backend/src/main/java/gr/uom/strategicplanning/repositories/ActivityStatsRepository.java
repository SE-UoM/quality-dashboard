package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.ActivityStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityStatsRepository extends JpaRepository<ActivityStats, Long> {

}
