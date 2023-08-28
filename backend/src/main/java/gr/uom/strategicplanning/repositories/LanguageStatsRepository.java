package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.LanguageStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageStatsRepository extends JpaRepository<LanguageStats, Long> {

}
