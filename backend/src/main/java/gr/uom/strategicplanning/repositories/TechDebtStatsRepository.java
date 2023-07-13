package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.TechDebtStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechDebtStatsRepository extends JpaRepository<TechDebtStats, Long> {
}
