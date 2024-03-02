package uom.qualitydashboard.organizationanalysisservice.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.organizationanalysisservice.clients.GithubAnalysisMicroserviceClient;
import uom.qualitydashboard.organizationanalysisservice.clients.OrganizationMicroserviceClient;
import uom.qualitydashboard.organizationanalysisservice.clients.SubmittedProjectsMicroservice;
import uom.qualitydashboard.organizationanalysisservice.models.GithubAnalysisDTO;
import uom.qualitydashboard.organizationanalysisservice.models.OrganizationAnalysis;
import uom.qualitydashboard.organizationanalysisservice.models.SubmittedProjectDTO;
import uom.qualitydashboard.organizationanalysisservice.repositories.OrganizationAnalysisRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationAnalysisService {
    private final OrganizationAnalysisRepository organizationAnalysisRepository;
    private final SubmittedProjectsMicroservice submittedProjectsMicroservice;
    private final GithubAnalysisMicroserviceClient githubAnalysisMicroserviceClient;
    private final OrganizationMicroserviceClient organizationMicroserviceClient;

    public OrganizationAnalysis startAnalysis(Long organizationId) {
        Collection<SubmittedProjectDTO> submittedProjects = submittedProjectsMicroservice.getSubmittedProjectsByOrganizationId(organizationId);

        int totalProjects = submittedProjects.size();
        int totalLanguages = 0;
        int totalCommits = 0;
        int totalFiles = 0;
        int totalLinesOfCode = 0;
        int totalDevs = 0;

        OrganizationAnalysis organizationAnalysis = null;

        for (SubmittedProjectDTO project : submittedProjects) {
            Long currentProjectId = project.getId();
            Optional<GithubAnalysisDTO> githubAnalysisOptional =  githubAnalysisMicroserviceClient.getLatestProjectAnalysis(currentProjectId);

            if (githubAnalysisOptional.isEmpty()) throw new EntityNotFoundException("No analysis found for project with id " + currentProjectId);

            GithubAnalysisDTO githubAnalysis = githubAnalysisOptional.get();

            Long organizationID = project.getOrganizationId();

            totalCommits += githubAnalysis.getTotalCommits();

            OrganizationAnalysis analysis = OrganizationAnalysis.builder()
                    .organizationId(organizationID)
                    .organizationName(organizationMicroserviceClient.getOrganizationById(organizationID))
                    .date(new Date())
                    .totalProjects(totalProjects)
                    .totalLanguages(totalLanguages)
                    .totalCommits(totalCommits)
                    .totalFiles(totalFiles)
                    .totalLinesOfCode(totalLinesOfCode)
                    .totalDevs(totalDevs)
                    .build();

            organizationAnalysis = organizationAnalysisRepository.save(analysis);
        }

        return organizationAnalysis;
    }
}
