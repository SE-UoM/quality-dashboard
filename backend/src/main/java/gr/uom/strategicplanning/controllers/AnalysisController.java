package gr.uom.strategicplanning.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import gr.uom.strategicplanning.analysis.external.*;
import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.strategies.PyAssessServiceStrategy;
import gr.uom.strategicplanning.controllers.responses.ErrorResponse;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.services.*;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import gr.uom.strategicplanning.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final ProjectRepository projectRepository;
    private final OrganizationService organizationService;
    private final ProjectService projectService;
    private final UserService userService;
    private final OrganizationAnalysisService organizationAnalysisService;
    private final String GITHUB_URL_PATTERN = "https://github.com/[^/]+/[^/]+" ;

    private final String PYASSESS_URL = "http://localhost:5000/api/analysis";

    private final String CODE_INSPECTOR_URL = "http://localhost:8000/api/analysis/prioritize_hotspots";

    private final ExternalServiceClient externalServiceClient;

    private PyAssessServiceStrategy pyAssessServiceStrategy;
    private CodeInspectorServiceStrategy codeInspectorServiceStrategy;

    @Autowired
    public AnalysisController(AnalysisService analysisService, OrganizationService organizationService,
                              UserService userService, ProjectService projectService,
                              ProjectRepository projectRepository, OrganizationAnalysisService organizationAnalysisService, ExternalServiceClient externalServiceClient,
                              PyAssessServiceStrategy pyAssessServiceStrategy) {
        this.analysisService = analysisService;
        this.projectService = projectService;
        this.userService = userService;
        this.organizationAnalysisService = organizationAnalysisService;
        this.organizationService = organizationService;
        this.projectRepository = projectRepository;
        this.externalServiceClient = externalServiceClient;
        this.pyAssessServiceStrategy = pyAssessServiceStrategy;
    }

    private boolean urlIsValid(String url) {
        return url.matches(GITHUB_URL_PATTERN);
    }

    @PostMapping("/start")
    public ResponseEntity startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) throws Exception {
        try{
            DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(request.getHeader("AUTHORIZATION"));
            String email = decodedJWT.getSubject();
            User user = userService.getUserByEmail(email);
            Organization organization = user.getOrganization();

            boolean urlIsInvalid = !urlIsValid(githubUrl);
            if (urlIsInvalid)
                return ResponseEntity.badRequest().
                        body(new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Invalid github url",
                                "The url you provided is not a valid github url"
                        ));


            Project project = new Project();
            project.setRepoUrl(githubUrl);
            project.setOrganization(organization);

            Optional<Project> projectOptional = projectRepository.findFirstByRepoUrl(githubUrl);

            organization.addProject(project);

            if (projectOptional.isPresent()) project = projectOptional.get();

            analysisService.fetchGithubData(project);

            if (!project.canBeAnalyzed()) {
                project.setStatus(ProjectStatus.ANALYSIS_TO_BE_REVIEWED);
                return ResponseEntity.ok("Project has been added to the queue");
            }

            analysisService.startAnalysis(project);
            externalServiceClient.analyzeWithExternalServices(project);

            organizationAnalysisService.updateOrganizationAnalysis(organization);
            organizationService.saveOrganization(organization);

            return ResponseEntity.ok(Collections.singletonMap("message", "Analysis ended successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
