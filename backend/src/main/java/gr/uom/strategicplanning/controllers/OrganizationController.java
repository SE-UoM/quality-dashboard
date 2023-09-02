package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.dtos.ActivityStatsDTO;
import gr.uom.strategicplanning.controllers.dtos.GeneralStatsDTO;
import gr.uom.strategicplanning.controllers.dtos.TechDebtStatsDTO;
import gr.uom.strategicplanning.controllers.responses.*;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.services.OrganizationService;
import gr.uom.strategicplanning.utils.TechDebtUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationService organizationService;

    private final String EURO = " €";

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
            try {
                List<Organization> organizations = organizationRepository.findAll();
                List<OrganizationResponse> organizationResponses = organizations.stream()
                        .map(OrganizationResponse::new)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(organizationResponses);
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);
            Organization organization = organizationOptional.orElseThrow(() -> new RuntimeException("Organization not found"));

            OrganizationResponse organizationResponse = new OrganizationResponse(organization);
            return ResponseEntity.ok(organizationResponse);
        }

    @GetMapping("/{id}/projects-info")
    public ResponseEntity<Collection<Map>> getOrganizationProjectsInfo(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            Collection<Project> organizationProjects = organization.getProjects();
            Collection<Map> projectsInfoResponse = new ArrayList<>();

            for (Project project : organizationProjects) {
                String projectName = project.getName();
                int projectCommits = project.getTotalCommits();
                int getProjectStars = project.getStars();
                int projectForks = project.getForks();

                Map<String, Object> projectInfoMap = new HashMap<>();
                projectInfoMap.put("name", projectName);
                projectInfoMap.put("totalContributions", projectCommits);
                projectInfoMap.put("stars", getProjectStars);
                projectInfoMap.put("forks", projectForks);

                projectsInfoResponse.add(projectInfoMap);
            }

            return ResponseEntity.ok(projectsInfoResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/project-names")
    public ResponseEntity<Collection<Map>> getOrganizationProjectNames(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            Collection<Project> organizationProjects = organization.getProjects();
            Collection<Map> projectsInfoResponse = new ArrayList<>();

            for (Project project : organizationProjects) {
                String projectName = project.getName();

                Map<String, Object> projectInfoMap = new HashMap<>();
                projectInfoMap.put("name", projectName);

                projectsInfoResponse.add(projectInfoMap);
            }

            return ResponseEntity.ok(projectsInfoResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/general-stats")
    public ResponseEntity<GeneralStatsDTO> getOrganizationAnalysisGeneralStats(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            GeneralStats generalStats = organizationAnalysis.getGeneralStats();

            GeneralStatsDTO generalStatsDTO = new GeneralStatsDTO(generalStats);

            return ResponseEntity.ok(generalStatsDTO);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/organization-analysis")
    public ResponseEntity<OrganizationAnalysisResponse> getOrganizationAnalysis(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);

            if (organizationOptional.isEmpty())
                return ResponseEntity.notFound().build();

            Organization organization = organizationOptional.get();

            OrganizationAnalysisResponse organizationAnalysisResponse = new OrganizationAnalysisResponse(organization.getOrganizationAnalysis());

            return ResponseEntity.ok(organizationAnalysisResponse);
        }

    @GetMapping("/{id}/top-developers")
    public ResponseEntity<Collection<DeveloperResponse>> getTopDevelopersByOrganizationId(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            Collection<Developer> projectDevelopers = organization.getProjects().stream()
                    .flatMap(project -> project.getDevelopers().stream())
                    .collect(Collectors.toList());

            List<Developer> topDeveloper = projectDevelopers.stream()
                    .sorted(Comparator.comparingDouble(Developer::getCodeSmellsPerCommit))
                    .collect(Collectors.toList());

            List<DeveloperResponse> developerResponses = topDeveloper.stream()
                    .map(DeveloperResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(developerResponses);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/language-names")
    public ResponseEntity<Collection<String>> getOrganizationLanguageNames(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Collection<OrganizationLanguage> organizationLanguages = organizationAnalysis.getLanguages();

            Collection<String> languageNamesResponse = organizationLanguages.stream()
                    .map(OrganizationLanguage::getName)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(languageNamesResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/top-projects")
    public ResponseEntity<Collection<Map>> getOrganizationTopProjects(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();
            Collection<Project> topProjects = techDebtStats.getBestTechDebtProjects();

            Collection<Map> topProjectsResponse = new ArrayList<>();

            for (Project project : topProjects) {
                Map<String, Object> projectResponse = new HashMap<>();

                String projectName = project.getName();
                double techDebt = project.getProjectStats().getTechDebtPerLoC();

                projectResponse.put("name", projectName);
                projectResponse.put("techDebtPerLoc", techDebt);

                topProjectsResponse.add(projectResponse);
            }

            return ResponseEntity.ok(topProjectsResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/developers-info")
    public ResponseEntity<Collection<Map>> getOrganizationDevelopersInfo(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            Collection<Project> organizationProjects = organization.getProjects();

            Collection<Map> developersInfoResponse = new ArrayList<>();
            for (Project project : organizationProjects) {
                Collection<Developer> projectDevelopers = project.getDevelopers();

                for (Developer developer : projectDevelopers) {
                    String developerName = developer.getName();
                    String avatarUrl = developer.getAvatarUrl();

                    Map<String, Object> developerInfo = new HashMap<>();
                    developerInfo.put("name", developerName);
                    developerInfo.put("avatarUrl", avatarUrl);

                    developersInfoResponse.add(developerInfo);
                }
            }

            return ResponseEntity.ok(developersInfoResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/most-active-developer")
    public ResponseEntity<Map> getMostActiveDeveloper(@PathVariable Long id) {
        try{
            Organization organization = organizationService.getOrganizationById(id);
            Collection<Project> organizationProjects = organization.getProjects();

            Developer mostActiveDeveloper = organizationProjects.stream()
                    .flatMap(project -> project.getDevelopers().stream())
                    .max(Comparator.comparingInt(Developer::getTotalCommits))
                    .orElse(null);

            Map<String, Object> mostActiveDeveloperResponse = new HashMap<>();
            mostActiveDeveloperResponse.put("name", mostActiveDeveloper.getName());
            mostActiveDeveloperResponse.put("totalCommits", mostActiveDeveloper.getTotalCommits());
            mostActiveDeveloperResponse.put("totalIssues", mostActiveDeveloper.getTotalCodeSmells());
            mostActiveDeveloperResponse.put("issuesPerContribution", mostActiveDeveloper.getCodeSmellsPerCommit());

            return ResponseEntity.ok(mostActiveDeveloperResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/most-active-project")
    public ResponseEntity<Map> getMostActiveProject(@PathVariable Long id) {
        try{
            Organization organization = organizationService.getOrganizationById(id);
            Collection<Project> organizationProjects = organization.getProjects();

            Project mostActiveProject = organizationProjects.stream()
                    .max(Comparator.comparingInt(Project::getTotalCommits))
                    .orElse(null);

            Map<String, Object> mostActiveProjectResponse = createSimpleProjectResponse(mostActiveProject);

            return ResponseEntity.ok(mostActiveProjectResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/most-starred-project")
    public ResponseEntity<Map> getMostStarredProject(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Project mostStarredProject = organizationAnalysis.getMostStarredProject();

            Map<String, Object> mostStarredProjectResponse = createSimpleProjectResponse(mostStarredProject);

            return ResponseEntity.ok(mostStarredProjectResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/most-forked-project")
    public ResponseEntity<Map> getMostForkedProject(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Project mostForkedProject = organizationAnalysis.getMostForkedProject();

            Map<String, Object> mostStarredProjectResponse = createSimpleProjectResponse(mostForkedProject);

            return ResponseEntity.ok(mostStarredProjectResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/last-analysis-date")
    public ResponseEntity<Map<String, Date>> getLastAnalysisDate(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Date lastAnalysisDate = organizationAnalysis.getAnalysisDate();

            Map<String, Date> lastAnalysisDateResponse = new HashMap<>();
            lastAnalysisDateResponse.put("lastAnalysisDate", lastAnalysisDate);

            return ResponseEntity.ok(lastAnalysisDateResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/top-languages")
    public ResponseEntity<Map> getTopOrganizationLanguages(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Map<Integer, OrganizationLanguage> topLanguages = organizationAnalysis.getTopLanguages();

            Map<String, LanguageResponse> topLanguagesResponse = new HashMap<>();
            for (Map.Entry<Integer, OrganizationLanguage> entry : topLanguages.entrySet()) {
                OrganizationLanguage currentLanguage = entry.getValue();
                Integer rank = entry.getKey();

                LanguageResponse languageResponse = new LanguageResponse(currentLanguage);
                topLanguagesResponse.put(rank.toString(), languageResponse);
            }

            return ResponseEntity.ok(topLanguagesResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/total-tech-debt")
    public ResponseEntity<Map> getTotalTechDebt(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();

            float tdInMinutes = techDebtStats.getTotalTechDebt();
            Number techDebtHours = TechDebtUtils.convertTDToHours(tdInMinutes);
            double techDectCostPerHour = TechDebtUtils.calculateTecDebtCostPerHour(techDebtHours);
            double techDebtCostPerMonth = TechDebtUtils.calculateTechDebtCostPerMonth(techDebtHours, techDectCostPerHour);

            Map<String, Object> totalTechDebtResponse = new HashMap<>();
            totalTechDebtResponse.put("totalTechDebtHours", techDebtHours);
            totalTechDebtResponse.put("techDebtCostPerHour", techDectCostPerHour);
            totalTechDebtResponse.put("techDebtCostPerMonth", techDebtCostPerMonth);
            totalTechDebtResponse.put("tdInMinutes", tdInMinutes);

            return ResponseEntity.ok(totalTechDebtResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/tech-debt-statistics")
    public ResponseEntity<Map> getTechDebtStats(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();

            int minProjectTechDebt = techDebtStats
                    .getProjectWithMinTechDebt()
                    .getProjectStats()
                    .getTechDebt();

            int maxProjectTechDebt = techDebtStats
                    .getProjectWithMaxTechDebt()
                    .getProjectStats()
                    .getTechDebt();

            float avgTechDebt = techDebtStats.getAverageTechDebt();
            float avgTechDebtPerLOC = techDebtStats.getAverageTechDebtPerLoC();

            Map<String, Object> techDebtStatsResponse = new HashMap<>();
            techDebtStatsResponse.put("minProjectTechDebt", minProjectTechDebt);
            techDebtStatsResponse.put("maxProjectTechDebt", maxProjectTechDebt);
            techDebtStatsResponse.put("avgTechDebt", avgTechDebt);
            techDebtStatsResponse.put("avgTechDebtPerLOC", avgTechDebtPerLOC);

            return ResponseEntity.ok(techDebtStatsResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/language-distribution")
    public ResponseEntity<Collection<LanguageResponse>> getOrganizationLanguageDistribution(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Collection<OrganizationLanguage> languages = organizationAnalysis.getLanguages();

            Collection<LanguageResponse> languageDistribution =
                    languages.stream()
                            .map(LanguageResponse::new)
                            .collect(Collectors.toList());

            return ResponseEntity.ok(languageDistribution);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/code-smells-distribution")
    public ResponseEntity<Collection<OrganizationCodeSmellDistribution>> getCodeSmellsDistribution(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();

            Collection<OrganizationCodeSmellDistribution> codeSmellsDistribution = techDebtStats.getCodeSmells();

            return ResponseEntity.ok(codeSmellsDistribution);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/all-commits")
    public ResponseEntity<Collection<Map>> getAllOrganizationCommits(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            Collection<Project> projects = organization.getProjects();

            Collection<Map> allCommits = new ArrayList<>();

            for (Project project : projects) {
                Collection<Commit> commits = project.getCommits();

                for (Commit commit : commits) {
                    String commitSHA = commit.getHash();
                    Date commitDate = commit.getCommitDate();

                    Map<String, Object> commitResponse = new HashMap<>();
                    commitResponse.put("sha", commitSHA);
                    commitResponse.put("date", commitDate);

                    allCommits.add(commitResponse);
                }
            }

            return ResponseEntity.ok(allCommits);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/activity")
    public ResponseEntity<ActivityStatsDTO> getOrganizationActivityStats(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            ActivityStats activityStats = organizationAnalysis.getActivityStats();

            ActivityStatsDTO activityStatsDTO = new ActivityStatsDTO(activityStats);

            return ResponseEntity.ok(activityStatsDTO);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, Object> createSimpleProjectResponse(Project project) {
        ProjectStats projectStats = project.getProjectStats();

        String projectName = project.getName();
        int projectStars = project.getStars();
        int totalFiles = projectStats.getTotalFiles();
        int totalLoC = projectStats.getTotalLoC();
        int techDebt = projectStats.getTechDebt();
        int codeSmells = projectStats.getTotalCodeSmells();
        int totalCommits = project.getTotalCommits();

        Map<String, Object> simpleProjectResponse = new HashMap<>();
        simpleProjectResponse.put("name", projectName);
        simpleProjectResponse.put("stars", projectStars);
        simpleProjectResponse.put("files", totalFiles);
        simpleProjectResponse.put("loc", totalLoC);
        simpleProjectResponse.put("techDebt", techDebt);
        simpleProjectResponse.put("totalCodeSmells", codeSmells);
        simpleProjectResponse.put("totalCommits", totalCommits);

        return simpleProjectResponse;
    }
}
