package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.Developer;
import gr.uom.strategicplanning.models.GeneralStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralStatsRepository extends JpaRepository<GeneralStats, Long> {
}
