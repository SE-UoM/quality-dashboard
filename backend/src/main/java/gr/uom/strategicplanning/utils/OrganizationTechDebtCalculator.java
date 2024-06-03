package gr.uom.strategicplanning.utils;

import gr.uom.strategicplanning.models.domain.Project;

import java.util.*;
import java.util.stream.Collectors;

public class OrganizationTechDebtCalculator {
    private OrganizationTechDebtCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static float calculateAvgTD(List<Project> projects) {
        return (float) projects.stream()
                .mapToDouble(project -> project.getProjectStats().getTechDebt())
                .average()
                .orElse(0);
    }

    public static int calculateTotalLOC(List<Project> projects) {
        return projects.stream()
                .map(project -> project.getProjectStats().getTotalLoC())
                .reduce(0, Integer::sum);
    }

    public static float calculateTechDebtPerLOC(List<Project> projects) {
        int totalLoC = calculateTotalLOC(projects);
        int totalTD = calculateTotalTD(projects);

        return (float) totalTD / totalLoC;
    }

    public static Optional<Project> findProjectWithMinTD(List<Project> projects) {
        return projects.stream()
                .min(Comparator.comparingInt(project -> project.getProjectStats().getTechDebt()));
    }

    public static Optional<Project> findProjectWithMaxTD(List<Project> projects) {
        return projects.stream()
                .max(Comparator.comparingInt(project -> project.getProjectStats().getTechDebt()));
    }

    public static int calculateTotalTD(Collection<Project> projects) {
        return projects.stream()
                .map(project -> project.getProjectStats().getTechDebt())
                .reduce(0, Integer::sum);
    }

    public static Collection<Project> findBestTechDebtProjects(Collection<Project> projects, int numberOfProjects) {
        Collection<Project> sortedProjects = projects.stream()
                .sorted(Comparator.comparing(project -> project.getProjectStats().getTechDebtPerLoC()))
                .collect(Collectors.toList());

        return sortedProjects.stream().limit(numberOfProjects).collect(Collectors.toList());
    }

    public static Collection<Project> findBestCodeSmellProjects(Collection<Project> projects, int totalProjects) {
        Collection<Project> sortedProjects = projects.stream()
                .sorted(Comparator.comparing(project -> project.getProjectStats().getTotalCodeSmells()))
                .collect(Collectors.toList());

        return sortedProjects.stream().limit(totalProjects).collect(Collectors.toList());
    }

    public static int calculateTotalCodeSmells(Collection<Project> projects) {
        return projects.stream()
                .map(project -> project.getProjectStats().getTotalCodeSmells())
                .reduce(0, Integer::sum);
    }

    public static Map<String, Integer> findCodeSmellsDistribution(Collection<Project> projects) {
        Map<String, Integer> codeSmellsDistributionMap = new HashMap<>();

        projects.stream()
                .forEach(project -> project.getProjectStats().getCodeSmellDistributions()
                        .forEach(codeSmellDistribution -> {
                            int currentMapValue = codeSmellsDistributionMap.getOrDefault(codeSmellDistribution.getCodeSmell(), 0);

                            codeSmellsDistributionMap.put(
                                    codeSmellDistribution.getCodeSmell(),
                                    currentMapValue + codeSmellDistribution.getCount()
                            );
                        }));

        return codeSmellsDistributionMap;
    }
}
