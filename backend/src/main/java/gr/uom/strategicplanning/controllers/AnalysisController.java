package gr.uom.strategicplanning.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.services.*;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import gr.uom.strategicplanning.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
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

    @Autowired
    public AnalysisController(AnalysisService analysisService, OrganizationService organizationService,
                              UserService userService, ProjectService projectService,
                              ProjectRepository projectRepository, OrganizationAnalysisService organizationAnalysisService) {
        this.analysisService = analysisService;
        this.projectService = projectService;
        this.userService = userService;
        this.organizationAnalysisService = organizationAnalysisService;
        this.organizationService = organizationService;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/start")
    public ResponseEntity startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) throws Exception {
        try{
            DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(request.getHeader("AUTHORIZATION"));
            String email = decodedJWT.getSubject();
            User user = userService.getUserByEmail(email);
            Organization organization = user.getOrganization();

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
            organizationAnalysisService.updateOrganizationAnalysis(organization);
            organizationService.saveOrganization(organization);

            return ResponseEntity.ok(Collections.singletonMap("message", "Analysis ended successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
