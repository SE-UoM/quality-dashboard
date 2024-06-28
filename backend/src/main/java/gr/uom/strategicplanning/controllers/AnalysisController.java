package gr.uom.strategicplanning.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.handlers.AnalysisExceptionHandler;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/analysis")
@Slf4j
public class AnalysisController {
    private final AnalysisService analysisService;
    private final UserService userService;
    private final MailSendingService mailSendingService;
    private final AnalysisExceptionHandler analysisExceptionHandler = new AnalysisExceptionHandler();

    @Value("${frontend.url}")
    private String  frontendURL;

    @Autowired
    public AnalysisController(
            AnalysisService analysisService,
            UserService userService,
            MailSendingService mailSendingService
    ) {
        this.analysisService = analysisService;
        this.userService = userService;
        this.mailSendingService = mailSendingService;
    }

    @PostMapping("/start")
    public ResponseEntity<ResponseInterface> startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) {
        try {
            DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(request.getHeader("AUTHORIZATION"));
            String email = decodedJWT.getSubject();
            User user = userService.getUserByEmail(email);

            Project project = analysisService.analyzeProject(githubUrl, user);

            // Send a mail to the user to notify them that the analysis has finished
            mailSendingService.sendAnalysisCompletionEmail(email, project.getName(), frontendURL);

            // Return response immediately
            ResponseInterface response = ResponseFactory.createResponse(
                    HttpStatus.OK.value(),
                    "Analysis for project " + project.getName() + " finished! You can view the results in the dashboard."
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return analysisExceptionHandler.handle(e);
        }
    }
}
