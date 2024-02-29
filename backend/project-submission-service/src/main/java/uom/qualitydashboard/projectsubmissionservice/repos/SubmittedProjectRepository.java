package uom.qualitydashboard.projectsubmissionservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.qualitydashboard.projectsubmissionservice.models.SubmittedProject;

import java.util.Collection;
import java.util.Optional;

public interface SubmittedProjectRepository extends JpaRepository<SubmittedProject, Long> {
    Collection<SubmittedProject> findByUserId(Long userId);
    Collection<SubmittedProject> findByOrganizationId(Long organizationId);
    Optional<SubmittedProject> findByRepoUrl(String repoUrl);
}
