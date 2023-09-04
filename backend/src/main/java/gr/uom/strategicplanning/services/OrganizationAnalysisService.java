package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import gr.uom.strategicplanning.repositories.OrganizationAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrganizationAnalysisService {

    private OrganizationAnalysisRepository organizationAnalysisRepository;

    private GeneralStatsService generalStatsService;

    private ActivityStatsService activityStatsService;

    private TechDebtStatsService techDebtStatsService;
    @Autowired
    private LanguageService languageService;

    public OrganizationAnalysisService(OrganizationAnalysisRepository organizationAnalysisRepository,TechDebtStatsService techDebtStatsService, ActivityStatsService activityStatsService, GeneralStatsService generalStatsService) {
        this.organizationAnalysisRepository = organizationAnalysisRepository;
        this.generalStatsService = generalStatsService;
        this.activityStatsService = activityStatsService;
        this.techDebtStatsService = techDebtStatsService;
    }


    public void updateOrganizationAnalysis(Organization organization) {
        OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

        organizationAnalysis.setAnalysisDate(new java.util.Date());
        organizationAnalysis.setOrgName(organization.getName());
        organizationAnalysis.setMostForkedProject(getMostForkedProject(organization));

        organizationAnalysis.setMostStarredProject(getMostStarredProject(organization));

        Collection<Project> projects = organization.getProjects();

        languageService.updateOrganizationLanguages(organization);

        getGeneralStats(organization);
        organizationAnalysis.setActivityStats(getActivityStats(organization));

        TechDebtStats techDebtStats = organization.getOrganizationAnalysis().getTechDebtStats();
        organizationAnalysis.setTechDebtStats(techDebtStats);
        organizationAnalysis.setOrganization(organization);
        organization.setOrganizationAnalysis(organizationAnalysis);

        getTechDebtStats(organization);

        organization.getOrganizationAnalysis().findTopLanguages();

        saveOrganizationAnalysis(organizationAnalysis);
    }

    public void saveOrganizationAnalysis(OrganizationAnalysis organizationAnalysis) {
        organizationAnalysisRepository.save(organizationAnalysis);
    }

    private TechDebtStats getTechDebtStats(Organization organization) {
        return techDebtStatsService.getTechDebtStats(organization);
    }

    private ActivityStats getActivityStats(Organization organization) {
        return activityStatsService.getActivityStats(organization);
    }

    private GeneralStats getGeneralStats(Organization organization) {
        Collection<Project> organizationProjects = organization.getProjects();
        int totalProjects = organizationProjects.size();

        Collection<OrganizationLanguage> organizationLanguages = organization.getOrganizationAnalysis().getLanguages();
        int totalLanguages = organizationLanguages.size();

        Collection<Commit> organizationCommits = getAllOrganizationCommits(organization);
        int totalCommits = organizationCommits.size();

        Collection<Developer> organizationDevelopers = getAllOrganizationDevelopers(organization);
        int totalDevelopers = organizationDevelopers.size();

        int totalLinesOfCode = calculateTotalLinesOfCode(organization);

        int totalFiles = generalStatsService.countTotalFiles(organization);

        GeneralStats generalStats = organization.getOrganizationAnalysis().getGeneralStats();
        generalStats.setTotalProjects(totalProjects);
        generalStats.setTotalLanguages(totalLanguages);
        generalStats.setTotalCommits(totalCommits);
        generalStats.setTotalDevs(totalDevelopers);
        generalStats.setTotalLinesOfCode(totalLinesOfCode);
        generalStats.setTotalFiles(totalFiles);


        return generalStats;
    }

    private Collection<Developer> getAllOrganizationDevelopers(Organization organization) {
        Collection<Project> organizationProjects = organization.getProjects();
        Set<Developer> organizationDevelopers = new HashSet<>();

        for (Project project : organizationProjects) {
            Collection<Developer> projectDevelopers = project.getDevelopers();
            organizationDevelopers.addAll(projectDevelopers);
        }
        return organizationDevelopers;
    }

    private Collection<Commit> getAllOrganizationCommits(Organization organization) {
        Collection<Project> organizationProjects = organization.getProjects();
        Collection<Commit> organizationCommits = new ArrayList();

        for (Project project : organizationProjects) {
            Collection<Commit> projectCommits = project.getCommits();
            organizationCommits.addAll(projectCommits);
        }
        return organizationCommits;
    }

    private int calculateTotalLinesOfCode(Organization organization) {
        Collection<OrganizationLanguage> organizationLanguages = organization.getOrganizationAnalysis().getLanguages();
        int totalLinesOfCode = 0;

        for (OrganizationLanguage organizationLanguage : organizationLanguages) {
            totalLinesOfCode += organizationLanguage.getLinesOfCode();
        }

        return totalLinesOfCode;
    }

    private Project getMostStarredProject(Organization organization) {
        int maxStars = 0;
        Project mostStarredProject = organization.getFirstProject();

        // TODO: This is a bug. If there are two or more projects with the same number of forks, only one will be returned.
        for (Project project : organization.getProjects()) {
            if (project.getStars() > maxStars) {
                maxStars = project.getStars();
                mostStarredProject = project;
            }
        }

        return mostStarredProject;
    }

    private Project getMostForkedProject(Organization organization) {
        int maxForks = 0;

        // TODO: This is a bug. If there are two or more projects with the same number of forks, only one will be returned.
        Project mostForkedProject = organization.getFirstProject();
        for (Project project : organization.getProjects()) {
            if (project.getForks() >= maxForks) {
                maxForks = project.getForks();
                mostForkedProject = project;
            }
        }

        return mostForkedProject;
    }
}
