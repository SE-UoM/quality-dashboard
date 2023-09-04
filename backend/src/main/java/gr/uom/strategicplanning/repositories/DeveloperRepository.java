package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByName(String developerName);

    Collection<Developer> findAllByOrganizationId(Long organizationId);
}
