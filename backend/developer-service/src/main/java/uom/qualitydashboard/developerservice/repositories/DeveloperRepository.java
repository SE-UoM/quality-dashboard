package uom.qualitydashboard.developerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.qualitydashboard.developerservice.models.Developer;

import java.util.Collection;
import java.util.Optional;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findDeveloperByName(String name);
    Optional<Developer> findDeveloperByGithubProfileURL(String githubProfileURL);
    boolean existsByGithubProfileURL(String githubProfileURL);
}
