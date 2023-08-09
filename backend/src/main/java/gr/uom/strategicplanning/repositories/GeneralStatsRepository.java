package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneralStatsRepository extends JpaRepository<GeneralStats, Long> {
    Optional<GeneralStats> findByOrganization(Organization organization);
}
