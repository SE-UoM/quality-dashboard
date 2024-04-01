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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
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

    @Async
    @PostMapping("/start")
    public CompletableFuture<ResponseEntity<ResponseInterface>> startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) {
        try {
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

                return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(response));
            }

            // Start the background task
            CompletableFuture<Void> backgroundTask = CompletableFuture.runAsync(() -> {
                try {
                    Project project = new Project();
                    project.setRepoUrl(githubUrl);
                    project.setOrganization(organization);

                    Optional<Project> projectOptional = projectRepository.findFirstByRepoUrl(githubUrl);

                    organization.addProject(project);

                    if (projectOptional.isPresent()) project = projectOptional.get();

                    analysisService.fetchGithubData(project);

                    if (!project.canBeAnalyzed()) {
                        project.setStatus(ProjectStatus.ANALYSIS_TO_BE_REVIEWED);
                    } else {
                        analysisService.startAnalysis(project);
                    }

                    organizationAnalysisService.updateOrganizationAnalysis(organization);
                    organizationService.saveOrganization(organization);

                    if (EXTERNAL_ANALYSIS_IS_ACTIVATED) externalAnalysisService.analyzeWithExternalServices(project);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Return response immediately
            ResponseInterface response = ResponseFactory.createResponse(
                    HttpStatus.OK.value(),
                    "Analysis started in the background"
            );
            return CompletableFuture.completedFuture(ResponseEntity.ok(response));
        } catch (Exception e) {
            ResponseInterface response = ResponseFactory.createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Analysis failed",
                    e.getMessage()
            );
            e.printStackTrace();
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(response));
        }
    }
}
