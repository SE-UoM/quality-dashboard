package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.dtos.ActivityStatsDTO;
import gr.uom.strategicplanning.controllers.dtos.CommitDTO;
import gr.uom.strategicplanning.controllers.dtos.GeneralStatsDTO;
import gr.uom.strategicplanning.controllers.dtos.OrganizationCodeSmellDistribution;
import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.controllers.responses.implementations.*;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.*;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.services.CommitService;
import gr.uom.strategicplanning.services.DeveloperService;
import gr.uom.strategicplanning.services.OrganizationService;
import gr.uom.strategicplanning.utils.TechDebtUtils;
import org.eclipse.egit.github.core.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private DeveloperService developerService;
    @Autowired
    private CommitService commitService;

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

    @GetMapping("/public/names")
    public ResponseEntity<List<Map>> getPublicOrganizations() {
        try {
            List<Organization> organizations = organizationRepository.findAll();

            List<Map> publicOrganizations = new ArrayList<>();

            for (Organization organization : organizations) {
                Map<String, Object> organizationMap = new HashMap<>();
                organizationMap.put("id", organization.getId());
                organizationMap.put("name", organization.getName());
                organizationMap.put("imgURL", organization.getImgURL());

                publicOrganizations.add(organizationMap);
            }

            return ResponseEntity.ok(publicOrganizations);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);
            Organization organization = organizationOptional.orElseThrow(() -> new RuntimeException("Organization not found"));

            OrganizationResponse organizationResponse = new OrganizationResponse(organization);
            return ResponseEntity.ok(organizationResponse);
        }

    @GetMapping("/public/{id}/projects-info")
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
                String projectOwner = project.getOwnerName();

                Map<String, Object> projectInfoMap = new HashMap<>();
                projectInfoMap.put("name", projectName);
                projectInfoMap.put("owner", projectOwner);
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

    @GetMapping("/public/{id}/project-names")
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

    @GetMapping("/public/{id}/general-stats")
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

    @GetMapping("/public/{id}/organization-analysis")
    public ResponseEntity<OrganizationAnalysisResponse> getOrganizationAnalysis(@PathVariable Long id) {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);

            if (organizationOptional.isEmpty())
                return ResponseEntity.notFound().build();

            Organization organization = organizationOptional.get();

            OrganizationAnalysisResponse organizationAnalysisResponse = new OrganizationAnalysisResponse(organization.getOrganizationAnalysis());

            return ResponseEntity.ok(organizationAnalysisResponse);
        }

    @GetMapping("/public/{id}/top-developers")
    public ResponseEntity<Collection<DeveloperResponse>> getTopDevelopersByOrganizationId(@PathVariable Long id) {
        try {
            Collection<Developer> allDevelopers  = developerService.findAllByOrganizationId(id);

            List<Developer> topDeveloper = allDevelopers.stream()
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

    @GetMapping("/public/{id}/top-contributors")
    public ResponseEntity<Collection> getTopContributors(@PathVariable Long id) {
        try {
            Collection<Developer> allDevelopers  = developerService.findAllByOrganizationId(id);

            List<Developer> topDeveloper = allDevelopers.stream()
                    .sorted(Comparator.comparingDouble(Developer::getTotalCommits).reversed())
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

    @GetMapping("/public/{id}/language-names")
    public ResponseEntity<Collection<String>> getOrganizationLanguageNames(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Collection<OrganizationLanguage> organizationLanguages = organizationAnalysis.getLanguages();

            Collection<String> languageNamesResponse = new ArrayList<>();
            for (OrganizationLanguage organizationLanguage : organizationLanguages) {
                if (organizationLanguage.getName() != null)
                    languageNamesResponse.add(organizationLanguage.getName());
            }

            return ResponseEntity.ok(languageNamesResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/top-projects")
    public ResponseEntity<Collection<Map>> getOrganizationTopProjects(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);

            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();
            Collection<Project> topProjects = techDebtStats.getBestTechDebtProjects();

            Collection<Map> topProjectsResponse = new ArrayList<>();

            for (Project project : topProjects) {
                // If the analysis is not complete, skip the project
                if (project.getStatus() != ProjectStatus.ANALYSIS_COMPLETED) continue;

                Map<String, Object> projectResponse = new HashMap<>();

                String projectName = project.getName();
                String projectOwner = project.getOwnerName();
                String projectDescription = project.getProjectDescription();
                String defaultBranch = project.getDefaultBranchName();
                double techDebt = project.getProjectStats().getTechDebtPerLoC();

                projectResponse.put("name", projectName);
                projectResponse.put("owner", projectOwner);
                projectResponse.put("techDebtPerLoc", techDebt);
                projectResponse.put("description", projectDescription);
                projectResponse.put("defaultBranch", defaultBranch);

                topProjectsResponse.add(projectResponse);
            }

            return ResponseEntity.ok(topProjectsResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/developers-info")
    public ResponseEntity<Collection<Map>> getOrganizationDevelopersInfo(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            Collection<Project> organizationProjects = organization.getProjects();

            Set<Map> developersInfoResponse = new HashSet<>();
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

    @GetMapping("/public/{id}/most-active-developer")
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
            mostActiveDeveloperResponse.put("avatarUrl", mostActiveDeveloper.getAvatarUrl());
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

    @GetMapping("/public/{id}/most-active-project")
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

    @GetMapping("/public/{id}/most-starred-project")
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

    @GetMapping("/public/{id}/most-forked-project")
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

    @GetMapping("/public/{id}/last-analysis-date")
    public ResponseEntity<Map<String, String>> getLastAnalysisDate(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Date lastAnalysisDate = organizationAnalysis.getAnalysisDate();

            // Convert to MM/DD/YYYY format
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = formatter.format(lastAnalysisDate);

            Map<String, String> lastAnalysisDateMap = new HashMap<>();
            lastAnalysisDateMap.put("lastAnalysisDate", formattedDate);

            return ResponseEntity.ok(lastAnalysisDateMap);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/top-languages")
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

    @GetMapping("/public/{id}/total-tech-debt")
    public ResponseEntity<Map> getTotalTechDebt(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();

            float tdInMinutes = techDebtStats.getTotalTechDebt();
            Number techDebtHours = TechDebtUtils.convertTDToHours(tdInMinutes);

            double costForAllHours = TechDebtUtils.calculateTechDebtForAllHours(techDebtHours);
            double techDebtCostPerMonth = TechDebtUtils.calculateTechDebtCostPerMonth(costForAllHours, techDebtHours);

            Map<String, Object> totalTechDebtResponse = new HashMap<>();
            totalTechDebtResponse.put("totalTechDebtHours", techDebtHours);
            totalTechDebtResponse.put("totalTechDebtCost", costForAllHours);
            totalTechDebtResponse.put("techDebtCostPerMonth", techDebtCostPerMonth);
            totalTechDebtResponse.put("tdInMinutes", tdInMinutes);

            return ResponseEntity.ok(totalTechDebtResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/tech-debt-statistics")
    public ResponseEntity<Map> getTechDebtStats(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();

            double minProjectTechDebtInMins = techDebtStats
                    .getProjectWithMinTechDebt()
                    .getProjectStats().getTechDebtPerLoC();

            Number minProjectTechDebt = TechDebtUtils.convertTDToHours(minProjectTechDebtInMins);

            double costForAllHoursMin = TechDebtUtils.calculateTechDebtForAllHours(minProjectTechDebt);
            double tdCostPerMonthMin = TechDebtUtils.calculateTechDebtCostPerMonth(costForAllHoursMin, minProjectTechDebt);

            double maxProjectTechDebtInMins = techDebtStats
                    .getProjectWithMaxTechDebt()
                    .getProjectStats().getTechDebtPerLoC();

            Number maxProjectTechDebt = TechDebtUtils.convertTDToHours(maxProjectTechDebtInMins);

            double costForAllHoursMax = TechDebtUtils.calculateTechDebtForAllHours(maxProjectTechDebt);
            double tdCostPerMonthMax = TechDebtUtils.calculateTechDebtCostPerMonth(costForAllHoursMax, maxProjectTechDebt);

            float avgTechDebt = techDebtStats.getAverageTechDebt();
            float avgTechDebtPerLOC = techDebtStats.getAverageTechDebtPerLoC();

            Map<String, Object> techDebtStatsResponse = new HashMap<>();
            techDebtStatsResponse.put("minProjectTechDebtPerLOC", tdCostPerMonthMin);
            techDebtStatsResponse.put("maxProjectTechDebtPerLOC", tdCostPerMonthMax);
            techDebtStatsResponse.put("avgTechDebt", avgTechDebt);
            techDebtStatsResponse.put("avgTechDebtPerLOC", avgTechDebtPerLOC);

            return ResponseEntity.ok(techDebtStatsResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/language-distribution")
    public ResponseEntity<ResponseInterface> getOrganizationLanguageDistribution(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Collection<OrganizationLanguage> languages = organizationAnalysis.getLanguages();

            Collection<LanguageResponse> languageDistribution = getLanguageDistribution(languages);
            int totalLanguages = languageDistribution.size();

            ResponseInterface languageDistributionResponse = ResponseFactory.createLanguageDistributionResponse(
                    totalLanguages,
                    languageDistribution
            );

            return ResponseEntity.ok(languageDistributionResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            ResponseInterface errorResponse = ResponseFactory.createErrorResponse(HttpStatus.NOT_FOUND.value(), "Organization not found", "Organization not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    }

    private Collection<LanguageResponse> getLanguageDistribution(Collection<OrganizationLanguage> languages) {
        Collection<LanguageResponse> languageDistribution = new ArrayList<>();

        for (OrganizationLanguage language : languages) {
            if (language.getName() != null && !language.getName().equals("none")) {
                LanguageResponse languageResponse = new LanguageResponse(language);
                languageDistribution.add(languageResponse);
            }
        }

        return languageDistribution;
    }

    @GetMapping("/public/{id}/code-smells-distribution")
    public ResponseEntity<Map> getCodeSmellsDistribution(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();

            Map<String, Object> codeSmellsDistributionResponse = new HashMap<>();

            int totalCodeSmells = techDebtStats.getTotalCodeSmells();
            codeSmellsDistributionResponse.put("totalCodeSmells", totalCodeSmells);

            Map<String, Integer> codeSmellsDistributionMap = techDebtStats.getCodeSmells();
            List<OrganizationCodeSmellDistribution> codeSmellsDistribution = new ArrayList<>();
            
            for (Map.Entry<String, Integer> entry : codeSmellsDistributionMap.entrySet()) {
                codeSmellsDistribution.add(new OrganizationCodeSmellDistribution(entry.getKey(),entry.getValue()));
            }

            codeSmellsDistributionResponse.put("codeSmellsDistribution", codeSmellsDistribution);

            return ResponseEntity.ok(codeSmellsDistributionResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/all-commits")
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

    @GetMapping("/public/{id}/activity")
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

    @GetMapping("/public/{id}/hotspots/distribution")
    public Map<String, Object> getOrganizationHotspotsDistribution(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            CodeInspectorStats codeInspectorStats = organizationAnalysis.getCodeInspectorStats();

            Map<String, Object> response = new HashMap<>();
            response.put("totalOutliers", codeInspectorStats.getTotalOutliers());
            response.put("totalHotspots", codeInspectorStats.getTotalHotspots());
            response.put("highPriorityHotspots", codeInspectorStats.getHighPriorityHotspots());
            response.put("mediumPriorityHotspots", codeInspectorStats.getMediumPriorityHotspots());
            response.put("normalPriorityHotspots", codeInspectorStats.getNormalPriorityHotspots());
            response.put("lowPriorityHotspots", codeInspectorStats.getLowPriorityHotspots());
            response.put("unknownPriorityHotspots", codeInspectorStats.getUnknownPriorityHotspots());

            return response;
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/public/{id}/coverage")
    public ResponseEntity<Map> getOrganizationCoverageDetails(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            PyAssessStats pyAssessStats = organizationAnalysis.getPyAssessStats();

            Map<String, Object> response = new HashMap<>();
            response.put("totalCoverage", pyAssessStats.getTotalCoverage());
            response.put("totalMiss", pyAssessStats.getTotalMiss());
            response.put("totalStmts", pyAssessStats.getTotalStmts());

            return ResponseEntity.ok(response);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/refactorings")
    public ResponseEntity<Map> getOrganizationRefactorings(@PathVariable Long id) {
        try {
            Map<String, Object> refactoringsResponse = new HashMap<>();
            refactoringsResponse.put("totalRefactorings", 50);
            refactoringsResponse.put("totalRefactoringsPerCommit", 3);

            return ResponseEntity.ok(refactoringsResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/dependencies")
    public ResponseEntity<Map> getOrganizationDependencies(@PathVariable Long id) {
        try {
            Optional<Organization> organizationOptional = organizationRepository.findById(id);

            if (organizationOptional.isEmpty())
                return ResponseEntity.notFound().build();

            Organization organization = organizationOptional.get();
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            PyAssessStats pyAssessStats = organizationAnalysis.getPyAssessStats();

            Map<String, Object> dependenciesResponse = new HashMap<>();
            dependenciesResponse.put("totalDependencies", pyAssessStats.getTotalDependencies());
            dependenciesResponse.put("dependencies", pyAssessStats.getDependencies());

            return ResponseEntity.ok(dependenciesResponse);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/commits/details")
    public ResponseEntity<Map> getOrganizationCommitsDetails(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            Collection<Project> projects = organization.getProjects();

            Map<String, Object> response = new HashMap<>();

            // Get all commits from the organization's projects and add them to the response
            Collection<CommitDTO> allCommits = new ArrayList<>();
            for (Project project : projects) {
                Collection<Commit> commits = project.getCommits();

                for (Commit commit : commits) {
                    CommitDTO commitDTO = CommitDTO.from(commit);
                    allCommits.add(commitDTO);
                }
            }

            // Sort the commits by date
            allCommits = allCommits.stream()
                    .sorted(Comparator.comparing(commit -> {
                        try {
                            return new SimpleDateFormat("yyyy-MM-dd").parse(commit.getDate());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }))
                    .collect(Collectors.toList());

            // Now based on all these commits calculate the organization's commits quality distribution
            // Quality types are: EXCELLENT, GOOD, FAIR, POOR, UNKNOWN
            Map<String, Integer> commitsQualityDistribution = new HashMap<>();
            int excellentCommits = 0;
            int goodCommits = 0;
            int fairCommits = 0;
            int poorCommits = 0;
            int unknownCommits = 0;

            for (CommitDTO commit : allCommits) {
                String maintainabilityRating = commit.getMaintainabilityRating();

                if (maintainabilityRating.equals("EXCELLENT")) excellentCommits++;
                else if (maintainabilityRating.equals("GOOD")) goodCommits++;
                else if (maintainabilityRating.equals("FAIR")) fairCommits++;
                else if (maintainabilityRating.equals("POOR")) poorCommits++;
                else unknownCommits++;
            }

            commitsQualityDistribution.put("EXCELLENT", excellentCommits);
            commitsQualityDistribution.put("GOOD", goodCommits);
            commitsQualityDistribution.put("FAIR", fairCommits);
            commitsQualityDistribution.put("POOR", poorCommits);
            commitsQualityDistribution.put("UNKNOWN", unknownCommits);

            response.put("commits", allCommits);
            response.put("totalCommits", allCommits.size());
            response.put("organization", organization.getName());
            response.put("commitsQuality", commitsQualityDistribution);


            return ResponseEntity.ok(response);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/issues")
    public ResponseEntity<Map> getOrganizationIssues(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

            Map<String, Object> response = new HashMap<>();
            response.put("openIssuesCount", organizationAnalysis.getTotalOpenIssues());
            response.put("closedIssuesCount", organizationAnalysis.getTotalClosedIssues());
            response.put("totalIssuesCount", organizationAnalysis.getTotalIssues());

            return ResponseEntity.ok(response);
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/{id}/commits/year/{year}")
    public Map getOrgCommitsPerYear(@PathVariable Long id, @PathVariable int year) {
        try {
            Collection<Commit> orgCommitsByYear = commitService.getOrgCommitsByYear(year, id);
            Map<String, Object> response = new HashMap<>();

            Collection<CommitDTO> commitsList = new ArrayList<>();
            for (Commit commit : orgCommitsByYear) {
                CommitDTO commitDTO = CommitDTO.from(commit);
                commitsList.add(commitDTO);
            }

            commitsList = commitsList.stream()
                    // Date is a string, so we need to convert it to a Date object
                    .sorted(Comparator.comparing(commit -> {
                        try {
                            return new SimpleDateFormat("yyyy-MM-dd").parse(commit.getDate());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }))
                    .collect(Collectors.toList());

            response.put("year", year);
            response.put("organization", organizationRepository.findById(id).get().getName());
            response.put("totalCommits", commitsList.size());
            response.put("commits", commitsList);

            return response;
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/public/{id}/most-common-code-smell")
    public Map<String, Object> getMostCommonCodeSmell(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganizationById(id);
            OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
            TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();

            Map<String, Integer> codeSmells = techDebtStats.getCodeSmells();
            Map.Entry<String, Integer> mostCommonCodeSmell = codeSmells.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .orElse(null);

            Map<String, Object> mostCommonCodeSmellResponse = new HashMap<>();
            mostCommonCodeSmellResponse.put("name", mostCommonCodeSmell.getKey());
            mostCommonCodeSmellResponse.put("count", mostCommonCodeSmell.getValue());

            return mostCommonCodeSmellResponse;
        }
        catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> createSimpleProjectResponse(Project project) {
        ProjectStats projectStats = project.getProjectStats();

        String projectName = project.getName();
        String projectOwner = project.getOwnerName();
        int projectStars = project.getStars();
        int totalFiles = projectStats.getTotalFiles();
        int totalLoC = projectStats.getTotalLoC();
        int techDebt = projectStats.getTechDebt();
        int codeSmells = projectStats.getTotalCodeSmells();
        int totalCommits = project.getTotalCommits();

        Map<String, Object> simpleProjectResponse = new HashMap<>();
        simpleProjectResponse.put("name", projectName);
        simpleProjectResponse.put("owner", projectOwner);
        simpleProjectResponse.put("stars", projectStars);
        simpleProjectResponse.put("files", totalFiles);
        simpleProjectResponse.put("loc", totalLoC);
        simpleProjectResponse.put("techDebt", techDebt);
        simpleProjectResponse.put("totalCodeSmells", codeSmells);
        simpleProjectResponse.put("totalCommits", totalCommits);
        simpleProjectResponse.put("totalForks", project.getForks());
        simpleProjectResponse.put("defaultBranch", project.getDefaultBranchName());
        simpleProjectResponse.put("description", project.getProjectDescription());

        return simpleProjectResponse;
    }
}
