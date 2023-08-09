package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityStatsRepository extends JpaRepository<ActivityStats, Long> {

    Optional<ActivityStats> findByOrganization(Organization organization);
}
