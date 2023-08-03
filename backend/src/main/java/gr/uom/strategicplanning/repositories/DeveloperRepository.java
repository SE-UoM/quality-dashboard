package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Developer findByName(String developerName);
}
