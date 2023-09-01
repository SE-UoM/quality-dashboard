package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.CodeSmellDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeSmellDistributionRepository extends JpaRepository<CodeSmellDistribution, Long> {
    Optional<CodeSmellDistribution> findByCodeSmell(String codeSmell);
}
