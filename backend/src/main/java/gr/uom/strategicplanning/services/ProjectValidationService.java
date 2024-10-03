package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.exceptions.AnalysisException;
import gr.uom.strategicplanning.models.domain.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectValidationService {
    @Autowired
    private GithubService githubService;
    private ProjectService projectService;

    public void validateProjectForAnalysis(Project project) {
        if (project.getStatus() == ProjectStatus.ANALYSIS_STARTED || project.getStatus() == ProjectStatus.ANALYSIS_IN_PROGRESS) {
            throw new AnalysisException(HttpStatus.BAD_REQUEST, "The project is already being analyzed");
        }
        if (project.getStatus() == ProjectStatus.ANALYSIS_TO_BE_REVIEWED) {
            throw new AnalysisException(HttpStatus.BAD_REQUEST, "The project needs to be reviewed by an Admin first.");
        }
    }

    public void validateCommitThreshold(Project project) throws Exception {
        if (!project.hasLessCommitsThanThreshold()) {
            log.error("AnalysisService - analyzeProject - The project has more commits than the threshold and needs to be reviewed");

            projectService.authorizeProjectForAnalysis(project.getId());

            throw new AnalysisException(HttpStatus.BAD_REQUEST, "The project has a lot of Commits.");
        }
    }

    public void validateGithubUrl(String githubUrl) {
        if (githubService.isGithubUrlValid(githubUrl)) {
            log.error("AnalysisService - analyzeProject - Invalid github url");
            throw new AnalysisException(HttpStatus.BAD_REQUEST, "Invalid github url");
        }
    }
}
