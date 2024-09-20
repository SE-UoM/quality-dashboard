package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.external.PyAssessProjectStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PyAssessProjectStatsRepository extends JpaRepository<PyAssessProjectStats, Long> {
}
