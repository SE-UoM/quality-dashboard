package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.OrganizationLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationLanguageRepository extends JpaRepository<OrganizationLanguage, Long> {

    @Query("SELECT language FROM OrganizationLanguage language WHERE language.name = ?1")
    Optional<OrganizationLanguage> findByName(String languageName);
}
