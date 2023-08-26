package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import gr.uom.strategicplanning.repositories.TechDebtStatsRepository;
import org.aspectj.bridge.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TechDebtStatsService {

    private TechDebtStatsRepository techDebtStatsRepository;

    @Autowired
    public TechDebtStatsService(TechDebtStatsRepository techDebtStatsRepository) {
        this.techDebtStatsRepository = techDebtStatsRepository;
    }

    public TechDebtStats getTechDebtStats(Organization organization) {
        TechDebtStats techDebtStats = new TechDebtStats();
        Optional<TechDebtStats> techDebtStatsOptional = techDebtStatsRepository.findByOrganizationAnalysis(organization.getOrganizationAnalysis());

        if (techDebtStatsOptional.isPresent()) {
            techDebtStats = techDebtStatsOptional.get();
        }

        List<Project> projects = organization.getProjects(); // Collect the projects for reuse
        float averageTechDebt = calculateAverageTechDebt(projects);

        Map<Project, Float> projectTechDebt = projects.stream()
                .collect(Collectors.toMap(project -> project, project -> (float) project.getProjectStats().getTechDebt()));

        float minTechDebtValue = projectTechDebt.values().stream().min(Float::compareTo).orElse(0f);

        Collection<Project> bestTechDebtProjects = projectTechDebt.entrySet().stream()
                .filter(entry -> entry.getValue().equals(minTechDebtValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Map<Project, Long> projectCodeSmellCounts = projects.stream()
                .collect(Collectors.toMap(
                        project -> project,
                        project -> project.getCommits().stream()
                                .flatMap(commit -> commit.getCodeSmells().stream())
                                .count()
                ));

        long minCodeSmells = projectCodeSmellCounts.values().stream()
                .min(Long::compareTo)
                .orElse(0L);

        Collection<Project> bestCodeSmellProjects = projectCodeSmellCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == minCodeSmells)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        techDebtStats.setTotalTechDebt(projectTechDebt.values().stream().reduce(0f, Float::sum));
        techDebtStats.setAverageTechDebt(averageTechDebt);
        techDebtStats.setAverageTechDebtPerLoC((float) (averageTechDebt / projects.stream().mapToDouble(project -> project.getProjectStats().getTotalLoC()).sum()));
        techDebtStats.setBestTechDebtProjects(bestTechDebtProjects);
        techDebtStats.setProjectWithMinTechDebt(Objects.requireNonNull(bestTechDebtProjects.stream().findFirst().map(Project::getProjectStats).orElse(null)).getProject());
        techDebtStats.setProjectWithMaxTechDebt(Objects.requireNonNull(projectTechDebt.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .map(Project::getProjectStats)
                .orElse(null)).getProject());
        techDebtStats.setCodeSmells(projects.stream()
                .flatMap(project -> project.getCommits().stream())
                .flatMap(commit -> commit.getCodeSmells().stream())
                .collect(Collectors.toList()));
        techDebtStats.setTotalCodeSmells(techDebtStats.getCodeSmells().size());
        techDebtStats.setBestCodeSmellProjects(bestCodeSmellProjects);

        techDebtStats.setOrganizationAnalysis(organization.getOrganizationAnalysis());

        saveTechDebtStats(techDebtStats);

        return techDebtStats;
    }

    private float calculateAverageTechDebt(List<Project> projects) {
        return (float) projects.stream()
                .flatMap(project -> project.getCommits().stream())
                .mapToDouble(Commit::getTechnicalDebt)
                .sum() / projects.size();
    }

    private void saveTechDebtStats(TechDebtStats techDebtStats) {
        techDebtStatsRepository.save(techDebtStats);
    }

}
