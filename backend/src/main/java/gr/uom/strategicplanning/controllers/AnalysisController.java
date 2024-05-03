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
            MailSendingService mailSendingService
    ) {
        this.analysisService = analysisService;
        this.userService = userService;
        this.organizationAnalysisService = organizationAnalysisService;
        this.organizationService = organizationService;
        this.projectRepository = projectRepository;
        this.externalAnalysisService = externalAnalysisService;
        this.mailSendingService = mailSendingService;
    }

    private boolean urlIsValid(String url) {
        return url.matches(GITHUB_URL_PATTERN);
    }

    @PostMapping("/start")
    public ResponseEntity<ResponseInterface> startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) {
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

                return ResponseEntity.badRequest().body(response);
            }

            // Check if the project already exists
            Optional<Project> projectOptional = projectRepository.findFirstByRepoUrl(githubUrl);
            Project project;

            if (!projectOptional.isPresent()) {
                // If the project doesn't exist, create and save it
                project = new Project();
                project.setRepoUrl(githubUrl);
                project.setOrganization(organization);
                project.setStatus(ProjectStatus.ANALYSIS_NOT_STARTED);
                projectRepository.save(project);
                organization.addProject(project);
            } else {
                // If the project exists, retrieve it from the database
                project = projectOptional.get();
                //toDo
                // check status
                // and stop if analysis running
            }

            // Make sure github can find the repo
            boolean repoFound = analysisService.validateUrlWithGithub(githubUrl);

            if (!repoFound) {
                ResponseInterface response = ResponseFactory.createErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Repository not found",
                        "The repository you provided was not found on github"
                );

                return ResponseEntity.badRequest().body(response);
            }

            // Get the github data to make sure the repo actually exists
            analysisService.fetchGithubData(project);
            // Save the project
            projectRepository.save(project);

            // Check if the project has less than the required number of commits
            //toDo
            // boolean projectNeedsReview = project.getCommits().size() > OrganizationAnalysis.COMMITS_THRESHOLD;
            boolean repoHasLessCommitsThanThreshold = analysisService.repoHasLessThatThresholdCommits(project);
            boolean projectNeedsReview = !repoHasLessCommitsThanThreshold && project.getStatus() != ProjectStatus.ANALYSIS_READY;

            if (projectNeedsReview) {
                project.setStatus(ProjectStatus.ANALYSIS_TO_BE_REVIEWED);

                // Save the project, organization and update the organization analysis
                projectRepository.save(project);
                organizationService.saveOrganization(organization);

                ResponseInterface response = ResponseFactory.createResponse(
                        HttpStatus.OK.value(),
                        "The Repo has a lot of commits, so we will review it first. We will notify you when the analysis is done"
                );

                return ResponseEntity.ok(response);
            }

            if (project.getStatus() == ProjectStatus.ANALYSIS_STARTED) {
                ResponseInterface response = ResponseFactory.createResponse(
                        HttpStatus.OK.value(),
                        "The analysis is already underway. We will notify you when it is done"
                );

                return ResponseEntity.ok(response);
            }

            analysisService.startAnalysis(project);

            organizationAnalysisService.updateOrganizationAnalysis(organization);
            organizationService.saveOrganization(organization);

            if (EXTERNAL_ANALYSIS_IS_ACTIVATED) externalAnalysisService.analyzeWithExternalServices(project);

            // Send a mail to the user to notify them that the analysis has finished
            mailSendingService.sendAnalysisCompletionEmail(email, project.getName(), frontendURL);

            // Return response immediately
            ResponseInterface response = ResponseFactory.createResponse(
                    HttpStatus.OK.value(),
                    "Analysis Finished! You can see the results on the dashboard"
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
