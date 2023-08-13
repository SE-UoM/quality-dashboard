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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Optional;

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
    public ResponseEntity<String> startAnalysis(@RequestParam("github_url") String githubUrl, HttpServletRequest request) throws Exception {
        DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(request.getHeader("AUTHORIZATION"));
        String email = decodedJWT.getSubject();
        User user = userService.getUserByEmail(email);
        Organization organization = user.getOrganization();

        Project project = new Project();
        Optional<Project> projectOptional = projectRepository.findFirstByRepoUrl(githubUrl);

        organization.addProject(project);
        project.setOrganization(organization);

        if (projectOptional.isEmpty()) {
            project.setRepoUrl(githubUrl);
        }
        else {
            project = projectOptional.get();
        }

        analysisService.fetchGithubData(project);

        if (project.canBeAnalyzed()) {
            analysisService.startAnalysis(project);
        }
        else {
            project.setStatus(ProjectStatus.ANALYSIS_TO_BE_REVIEWED);
            return ResponseEntity.ok("Project has been added to the queue");
        }

        organizationAnalysisService.updateOrganizationAnalysis(organization);
        organizationService.saveOrganization(organization);

        return ResponseEntity.ok("Analysis ended successfully");
    }
}
