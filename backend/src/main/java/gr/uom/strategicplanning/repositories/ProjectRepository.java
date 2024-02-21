package gr.uom.strategicplanning.repositories;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findFirstByRepoUrl(String githubUrl);
    Optional<Project> findByName(String name);

    List<Project> findAllByStatus(ProjectStatus status);
}
