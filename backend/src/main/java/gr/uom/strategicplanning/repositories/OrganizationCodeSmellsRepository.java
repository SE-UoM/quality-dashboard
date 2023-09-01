package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.OrganizationCodeSmellDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationCodeSmellsRepository extends JpaRepository<OrganizationCodeSmellDistribution, Long> {
}
