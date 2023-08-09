package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechDebtStatsRepository extends JpaRepository<TechDebtStats, Long> {

    Optional<TechDebtStats> findByOrganizationAnalysis(OrganizationAnalysis organizationAnalysis);
}
