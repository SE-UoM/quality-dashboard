package uom.qualitydashboard.usersservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.qualitydashboard.usersservice.models.OrganizationUser;

import java.util.Collection;
import java.util.Optional;

public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, Long> {
    Optional<OrganizationUser> findByEmail(String email);
    boolean existsByEmail(String email);
    Collection<OrganizationUser> findAllByOrganizationId(Long organizationId);
}
