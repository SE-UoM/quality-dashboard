package uom.qualitydashboard.githubanalysisservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.qualitydashboard.githubanalysisservice.models.GithubAnalysis;

import java.util.Collection;
import java.util.Optional;

public interface GithubAnalysisRepository extends JpaRepository<GithubAnalysis, Long> {
    Collection<GithubAnalysis> findAllByProjectId(Long projectId);
    Collection<GithubAnalysis> findByProjectName(String projectName);
    Collection<GithubAnalysis> findByProjectFullName(String projectFullName);
    Collection<GithubAnalysis> findByProjectUrl(String projectUrl);

    // Get the latest analysis for a project
    Optional<GithubAnalysis> findTopByProjectIdOrderByLastAnalysisDateDesc(Long projectId);
}
