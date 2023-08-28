package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import gr.uom.strategicplanning.repositories.OrganizationAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationAnalysisService {

    private OrganizationAnalysisRepository organizationAnalysisRepository;

    private GeneralStatsService generalStatsService;

    private ActivityStatsService activityStatsService;

    private TechDebtStatsService techDebtStatsService;

    public OrganizationAnalysisService(OrganizationAnalysisRepository organizationAnalysisRepository,TechDebtStatsService techDebtStatsService, ActivityStatsService activityStatsService, GeneralStatsService generalStatsService) {
        this.organizationAnalysisRepository = organizationAnalysisRepository;
        this.generalStatsService = generalStatsService;
        this.activityStatsService = activityStatsService;
        this.techDebtStatsService = techDebtStatsService;
    }


    public void updateOrganizationAnalysis(Organization organization) {
        Optional<OrganizationAnalysis> organizationAnalysisOptional = organizationAnalysisRepository.findByOrganization(organization);
        OrganizationAnalysis organizationAnalysis = new OrganizationAnalysis();

        if (organizationAnalysisOptional.isPresent())
            organizationAnalysis = organizationAnalysisOptional.get();

        organizationAnalysis.setAnalysisDate(new java.util.Date());
        organizationAnalysis.setOrgName(organization.getName());
        organizationAnalysis.setMostForkedProject(getMostForkedProject(organization));

        organizationAnalysis.setMostStarredProject(getMostStarredProject(organization));
        organizationAnalysis.setGeneralStats(getGeneralStats(organization));
        organizationAnalysis.setActivityStats(getActivityStats(organization));
        organizationAnalysis.setTechDebtStats(getTechDebtStats(organization));
        organizationAnalysis.setOrganization(organization);
        organization.setOrganizationAnalysis(organizationAnalysis);

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
        return generalStatsService.getGeneralStats(organization);
    }

    private Project getMostStarredProject(Organization organization) {
        int maxStars = 0;
        Project mostStarredProject = null;

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
        Project mostForkedProject = null;
        for (Project project : organization.getProjects()) {
            if (project.getForks() >= maxForks) {
                maxForks = project.getForks();
                mostForkedProject = project;
            }
        }

        return mostForkedProject;
    }
}
