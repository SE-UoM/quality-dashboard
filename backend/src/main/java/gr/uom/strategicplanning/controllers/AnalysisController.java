package gr.uom.strategicplanning.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.services.*;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import gr.uom.strategicplanning.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/analysis")
@Slf4j
public class AnalysisController {
    private static final long ONE_HOUR = 3600000;
    private static final long  TEN_SECONDS = 10000;
    private final AnalysisService analysisService;
    private final ProjectRepository projectRepository;
    private final OrganizationService organizationService;
    private final UserService userService;
    private final OrganizationAnalysisService organizationAnalysisService;
    private final String GITHUB_URL_PATTERN = "https://github.com/[^/]+/[^/]+" ;
    private final ExternalAnalysisService externalAnalysisService;

    @Value("${services.external.activated}")
    private boolean EXTERNAL_ANALYSIS_IS_ACTIVATED;

    @Autowired
    public AnalysisController(AnalysisService analysisService, OrganizationService organizationService,
                              UserService userService, ProjectRepository projectRepository,
                              OrganizationAnalysisService organizationAnalysisService, ExternalAnalysisService externalAnalysisService) {
        this.analysisService = analysisService;
        this.userService = userService;
        this.organizationAnalysisService = organizationAnalysisService;
        this.organizationService = organizationService;
        this.projectRepository = projectRepository;
        this.externalAnalysisService = externalAnalysisService;
    }

    private boolean urlIsValid(String url) {
        return url.matches(GITHUB_URL_PATTERN);
    }

    @PostMapping("/start")
    public ResponseEntity<ResponseInterface> startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) throws Exception {
        try{
            DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(request.getHeader("AUTHORIZATION"));
            String email = decodedJWT.getSubject();
            User user = userService.getUserByEmail(email);
            Organization organization = user.getOrganization();

            boolean urlIsInvalid = !urlIsValid(githubUrl);
            if (urlIsInvalid) {
                ResponseInterface response = ResponseFactory.createErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid github url",
                        "The url you provided is not a valid github url"
                );

                return ResponseEntity.badRequest().
                        body(response);
            }

            Project project = new Project();

            Optional<Project> projectOptional = projectRepository.findFirstByRepoUrl(githubUrl);
            if (projectOptional.isPresent()) project = projectOptional.get();

            project.setRepoUrl(githubUrl);
            project.setOrganization(organization);

            Date submittedDate = new Date();
            project.setSubmittedDate(submittedDate);

            organization.addProject(project);

            projectRepository.save(project);
            organizationService.saveOrganization(organization);

            // if (EXTERNAL_ANALYSIS_IS_ACTIVATED) externalAnalysisService.analyzeWithExternalServices(project);

            ResponseInterface response = ResponseFactory.createResponse(
                    HttpStatus.OK.value(),
                    "Project Submitted successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseInterface response = ResponseFactory.createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Analysis failed",
                    e.getMessage()
            );

            e.printStackTrace();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @Scheduled(fixedRate = ONE_HOUR)
    public void analysisCode() throws Exception {

        try {
            List<Project> projects = projectRepository.findAllByStatus(ProjectStatus.ANALYSIS_NOT_STARTED);

            log.info("Starting analysis for " + projects.size() + " projects");

            for (Project project : projects) {
                log.info("Starting analysis for " + project.getRepoUrl());

                log.info("Fetching github data for " + project.getRepoUrl());
                analysisService.fetchGithubData(project);

                Organization organization = project.getOrganization();

                log.info("Saving organization " + organization.getName());
                organizationService.saveOrganization(organization);

                log.info("Starting analysis for " + project.getName());
                // Calculate how long it takes to analyze a project
                long start = System.currentTimeMillis();

                analysisService.startAnalysis(project);

                long end = System.currentTimeMillis();
                int timeInMinutes = (int) ((end - start) / ONE_HOUR);

                log.info("Analysis for " + project.getName() + " finished in " + timeInMinutes + " minutes");

                organizationAnalysisService.updateOrganizationAnalysis(organization);
            }
        }
        catch (Exception e) {
            log.error("Analysis failed", e);

            e.printStackTrace();
        }
    }

}
