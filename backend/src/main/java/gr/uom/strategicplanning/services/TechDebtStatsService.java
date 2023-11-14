package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import gr.uom.strategicplanning.repositories.TechDebtStatsRepository;
import gr.uom.strategicplanning.utils.OrganizationTechDebtCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TechDebtStatsService {

    private TechDebtStatsRepository techDebtStatsRepository;
    private final int PROJECTS_TO_SHOW = 20;

    @Autowired
    public TechDebtStatsService(TechDebtStatsRepository techDebtStatsRepository) {
        this.techDebtStatsRepository = techDebtStatsRepository;
    }

    public TechDebtStats getTechDebtStats(Organization organization) {
        TechDebtStats techDebtStats = organization.getOrganizationAnalysis().getTechDebtStats();
        List<Project> projects = organization.getProjects();

        // Find and set the average tech debt
        float averageTechDebt = OrganizationTechDebtCalculator.calculateAvgTD(projects);
        techDebtStats.setAverageTechDebt(averageTechDebt);

        // Find and set the best tech debt projects
        Collection<Project> bestTechDebtProjects = OrganizationTechDebtCalculator.findBestTechDebtProjects(projects, PROJECTS_TO_SHOW);
        techDebtStats.setBestTechDebtProjects(bestTechDebtProjects);

        // Find and set the best code smell projects
        Collection<Project> bestCodeSmellProjects = OrganizationTechDebtCalculator.findBestCodeSmellProjects(projects, PROJECTS_TO_SHOW);
        techDebtStats.setBestCodeSmellProjects(bestCodeSmellProjects);

        // Find and set the project with the min tech debt
        Optional<Project> projectWithMinTD = OrganizationTechDebtCalculator.findProjectWithMinTD(projects);
        techDebtStats.setProjectWithMinTechDebt(projectWithMinTD.orElse(null));

        // Find and set the project with the max tech debt
        Optional<Project> projectWithMaxTD = OrganizationTechDebtCalculator.findProjectWithMaxTD(projects);
        techDebtStats.setProjectWithMaxTechDebt(projectWithMaxTD.orElse(null));

        // Find and set total tech debt
        float totalTD = OrganizationTechDebtCalculator.calculateTotalTD(projects);
        techDebtStats.setTotalTechDebt(totalTD);

        // Find and set the average tech debt per LoC
        float avgTechDebtPerLoC = OrganizationTechDebtCalculator.calculateAvgTechDebtPerLOC(projects);
        techDebtStats.setAverageTechDebtPerLoC(avgTechDebtPerLoC);

        // Find and set the total code smells
        int totalCodeSmells = OrganizationTechDebtCalculator.calculateTotalCodeSmells(projects);
        techDebtStats.setTotalCodeSmells(totalCodeSmells);

        // Find Code Smells Distribution for the whole organization
        Collection<Project> allProjects = organization.getProjects();

        // Find and set the code smell distribution
        Map<String, Integer> codeSmellDistributionMap = OrganizationTechDebtCalculator.findCodeSmellsDistribution(allProjects);
        techDebtStats.initCodeSmellsDistribution();

        for (Map.Entry<String, Integer> entry : codeSmellDistributionMap.entrySet()) {
            // Get the calculated code smell distribution values
            String severity = entry.getKey();
            int count = entry.getValue();

            techDebtStats.updateCodeSmellDistribution(severity, count);
        }

        techDebtStats.setOrganizationAnalysis(organization.getOrganizationAnalysis());
        saveTechDebtStats(techDebtStats);

        return techDebtStats;
    }

    private void saveTechDebtStats(TechDebtStats techDebtStats) {
        techDebtStatsRepository.save(techDebtStats);
    }

}
