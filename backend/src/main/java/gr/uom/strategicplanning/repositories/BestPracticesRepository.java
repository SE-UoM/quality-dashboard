package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.BestPractice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BestPracticesRepository extends JpaRepository<BestPractice, Long> {
    public Optional<BestPractice> findByTitle(String title);
}
