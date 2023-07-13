package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.OrganizationAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationAnalysisRepository extends JpaRepository<OrganizationAnalysis, Long> {
}
