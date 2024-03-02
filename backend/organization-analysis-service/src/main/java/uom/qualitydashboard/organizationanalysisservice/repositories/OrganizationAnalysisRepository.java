package uom.qualitydashboard.organizationanalysisservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.qualitydashboard.organizationanalysisservice.models.OrganizationAnalysis;

import java.util.Collection;
import java.util.Optional;

public interface OrganizationAnalysisRepository extends JpaRepository<OrganizationAnalysis, Long> {
    // Get Organization Analyses by Organization ID
    Collection<OrganizationAnalysis> findByOrganizationId(Long organizationId);

    // Get the Organization Analysis by Organization Name
    Collection<OrganizationAnalysis> findByOrganizationName(String organizationName);

    // Get the Latest Organization Analysis by Organization ID and Date
    Optional<OrganizationAnalysis> findTopByOrganizationIdOrderByDateDesc(Long organizationId);
}
