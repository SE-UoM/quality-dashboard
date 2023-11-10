package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.ProjectCodeSmellDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeSmellDistributionRepository extends JpaRepository<ProjectCodeSmellDistribution, Long> {
    Optional<ProjectCodeSmellDistribution> findByCodeSmell(String codeSmell);
}
