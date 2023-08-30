package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.ProjectLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectLanguageRepository extends JpaRepository<ProjectLanguage, Long> {

    @Query("SELECT pl FROM ProjectLanguage pl WHERE pl.project.id = ?1 AND pl.name = ?2")
    Optional<ProjectLanguage> findByProjectIdAndLanguage(Long projectId, String language);

}
