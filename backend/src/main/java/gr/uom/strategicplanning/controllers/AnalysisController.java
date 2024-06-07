package gr.uom.strategicplanning.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final OrganizationService organizationService;
    private final UserService userService;
    private final OrganizationAnalysisService organizationAnalysisService;
    private final String GITHUB_URL_PATTERN = "https://github.com/[^/]+/[^/]+" ;
    private final ExternalAnalysisService externalAnalysisService;
    private final MailSendingService mailSendingService;

    @Value("${services.external.activated}")
    private boolean EXTERNAL_ANALYSIS_IS_ACTIVATED;

    @Value("${frontend.url}")
    private String  frontendURL;

    @Autowired
    public AnalysisController(
            AnalysisService analysisService,
            OrganizationService organizationService,
            UserService userService,
            ProjectRepository projectRepository,
            OrganizationAnalysisService organizationAnalysisService,
            ExternalAnalysisService externalAnalysisService,
            MailSendingService mailSendingService,
            ProjectService projectService
    ) {
        this.analysisService = analysisService;
        this.userService = userService;
        this.organizationAnalysisService = organizationAnalysisService;
        this.organizationService = organizationService;
        this.projectRepository = projectRepository;
        this.externalAnalysisService = externalAnalysisService;
        this.mailSendingService = mailSendingService;
        this.projectService = projectService;
    }

    private boolean urlIsValid(String url) {
        return url.matches(GITHUB_URL_PATTERN);
    }

    @PostMapping("/start")
    public ResponseEntity<ResponseInterface> startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) {
        try {
            boolean urlIsInvalid = githubUrl==null || githubUrl.isEmpty() || !githubUrl.matches(GITHUB_URL_PATTERN);
            if (urlIsInvalid || !analysisService.validateUrlWithGithub(githubUrl)) {
                return ResponseEntity.badRequest().body(ResponseFactory.createErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid github url",
                        "The url you provided is not a valid github url or the repository was not found. Make sure you provide a valid public github url."
                ));
            }

            DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(request.getHeader("AUTHORIZATION"));
            String email = decodedJWT.getSubject();
            User user = userService.getUserByEmail(email);

            Organization organization = user.getOrganization();
            Project project = projectService.getOrCreateProject(githubUrl, organization);

            // Get the github data to make sure the repo actually exists
            analysisService.fetchGithubData(project);

            if (!project.hasLessCommitsThanThreshold()) {
                projectService.updateProjectStatus(project.getId(), ProjectStatus.ANALYSIS_TO_BE_REVIEWED);

                return ResponseEntity.ok(ResponseFactory.createResponse(
                        HttpStatus.OK.value(),
                        "The Repo has a lot of commits, so we will review it first. We will notify you when the analysis is done"
                ));
            }

            Integer analyzedCommits = analysisService.startAnalysis(project);

            if(analyzedCommits==0)
                return ResponseEntity.ok(ResponseFactory.createResponse(
                        HttpStatus.OK.value(),
                        "Analysis Finished! All commits are already analyzed, you can see the results on the dashboard"
                ));

            organizationAnalysisService.updateOrganizationAnalysis(organization);
            organizationService.saveOrganization(organization);

            if (EXTERNAL_ANALYSIS_IS_ACTIVATED) externalAnalysisService.analyzeWithExternalServices(project);

            // Send a mail to the user to notify them that the analysis has finished
            mailSendingService.sendAnalysisCompletionEmail(email, project.getName(), frontendURL);

            // Return response immediately
            ResponseInterface response = ResponseFactory.createResponse(
                    HttpStatus.OK.value(),
                    "Analysis Finished for "+analyzedCommits+" commits! You can see the results on the dashboard"
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
}
