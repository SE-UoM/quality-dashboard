package uom.qualitydashboard.organizationservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.qualitydashboard.organizationservice.models.Organization;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
