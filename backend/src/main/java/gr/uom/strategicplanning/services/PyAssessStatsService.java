package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.external.PyAssessProjectStats;
import gr.uom.strategicplanning.models.stats.PyAssessStats;
import gr.uom.strategicplanning.repositories.PyAssessProjectStatsRepository;
import gr.uom.strategicplanning.repositories.PyAssessStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PyAssessStatsService {
    @Autowired
    private PyAssessProjectStatsRepository pyAssessProjectStatsRepository;
    @Autowired
    private PyAssessStatsRepository pyAssessStatsRepository;

    public void updateProjectStats(Project project, Map analysisItems) {
        log.info("Updating PyAssess Project Stats");
        PyAssessProjectStats projectStats = project.getPyAssessProjectStats();

        this.updateMetrics(projectStats, analysisItems);
        this.updateDependencies(projectStats, analysisItems);

        pyAssessProjectStatsRepository.save(projectStats);
    }

    private void updateMetrics(PyAssessProjectStats projectStats, Map analysisItems) {
        int totalDepedenencies = (int) analysisItems.get("dependenciesCounter");
        int totalCoverage = (int) analysisItems.get("totalCoverage");
        int totalMiss = (int) analysisItems.get("totalMiss");
        int totalStmts = (int) analysisItems.get("totalStmts");

        Map<String, Object> metrics = (Map<String, Object>) analysisItems.get("projectQualityMetrics");

        int nom = (int) metrics.get("nom");
        int wac = (int) metrics.get("wac");
        int nocc = (int) metrics.get("nocc");
        int dit = (int) metrics.get("dit");
        double wmpc1 = (double) metrics.get("wmpc1");
        int wmpc2 = (int) metrics.get("wmpc2");
        int rfc = (int) metrics.get("rfc");
        int cbo = (int) metrics.get("cbo");
        int mpc = (int) metrics.get("mpc");
        int lcom = (int) metrics.get("lcom");

        projectStats.setTotalDependenencies(totalDepedenencies);
        projectStats.setTotalCoverage(totalCoverage);
        projectStats.setTotalMiss(totalMiss);
        projectStats.setTotalStmts(totalStmts);
        projectStats.setNom(nom);
        projectStats.setWac(wac);
        projectStats.setNocc(nocc);
        projectStats.setDit(dit);
        projectStats.setWmpc1(wmpc1);
        projectStats.setWmpc2(wmpc2);
        projectStats.setRfc(rfc);
        projectStats.setCbo(cbo);
        projectStats.setMpc(mpc);
        projectStats.setLcom(lcom);
    }

    private void updateDependencies(PyAssessProjectStats projectStats, Map analysisItems) {
        Collection<String> dependencies = (Collection<String>) analysisItems.get("dependencies");
        for (String dependency : dependencies) {
            projectStats.addDependency(dependency);
        }
    }

    public void updateOrganizationStats(Organization org) {
        log.info("Updating Organization Stats");
        Long orgAnalysisID = org.getOrganizationAnalysis().getId();
        Optional<PyAssessStats> pyAssessStats = pyAssessStatsRepository.findByOrganizationAnalysisId(orgAnalysisID);

        if (pyAssessStats.isEmpty()) throw new RuntimeException("Organization not found in PyAssessStats");

        PyAssessStats updatedStats = pyAssessStats.get();
        Map<String, Object> organizationLevelMetrics = this.calculateOrganizationLevelMetrics(org);

        updatedStats.setAverageCoverage((double) organizationLevelMetrics.get("AvgCoverage"));
        updatedStats.setAverageMiss((double) organizationLevelMetrics.get("AvgMiss"));
        updatedStats.setAverageStmts((double) organizationLevelMetrics.get("AvgMotalStmts"));
        updatedStats.setAverageNom((double) organizationLevelMetrics.get("AvgNom"));
        updatedStats.setAverageWac((double) organizationLevelMetrics.get("AvgWac"));
        updatedStats.setAverageNocc((double) organizationLevelMetrics.get("AvgNocc"));
        updatedStats.setAverageDit((double) organizationLevelMetrics.get("AvgDit"));
        updatedStats.setAverageWmpc1((double) organizationLevelMetrics.get("AvgWmpc1"));
        updatedStats.setAverageWmpc2((double) organizationLevelMetrics.get("AvgWmpc2"));
        updatedStats.setAverageRfc((double) organizationLevelMetrics.get("AvgRfc"));
        updatedStats.setAverageCbo((double) organizationLevelMetrics.get("AvgCbo"));
        updatedStats.setAverageMpc((double) organizationLevelMetrics.get("AvgMpc"));
        updatedStats.setAverageLcom((double) organizationLevelMetrics.get("AvgLcom"));
        updatedStats.setTotalDependencies((int) organizationLevelMetrics.get("totalDependencies"));

        pyAssessStatsRepository.save(updatedStats);
    }

    private Map<String, Object> calculateOrganizationLevelMetrics(Organization org) {
        Collection<Project> orgProjects = org.getProjects();
        int totalProjects = orgProjects.size();

        int totalCoverage = 0;
        int totalMiss = 0;
        int totalStmts = 0;
        int totalNom = 0;
        int totalWac = 0;
        int totalNocc = 0;
        int totalDit = 0;
        double totalWmpc1 = 0;
        int totalWmpc2 = 0;
        int totalRfc = 0;
        int totalCbo = 0;
        int totalMpc = 0;
        int totalLcom = 0;
        int totalDependencies = 0;

        for (Project project : orgProjects) {
            PyAssessProjectStats projectStats = project.getPyAssessProjectStats();

            totalCoverage += projectStats.getTotalCoverage();
            totalMiss += projectStats.getTotalMiss();
            totalStmts += projectStats.getTotalStmts();
            totalNom += projectStats.getNom();
            totalWac += projectStats.getWac();
            totalNocc += projectStats.getNocc();
            totalDit += projectStats.getDit();
            totalWmpc1 += projectStats.getWmpc1();
            totalWmpc2 += projectStats.getWmpc2();
            totalRfc += projectStats.getRfc();
            totalCbo += projectStats.getCbo();
            totalMpc += projectStats.getMpc();
            totalLcom += projectStats.getLcom();
            totalDependencies += projectStats.getTotalDependenencies();
        }

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("AvgCoverage", (double) totalCoverage / totalProjects);
        metrics.put("AvgMiss", (double) totalMiss / totalProjects);
        metrics.put("AvgMotalStmts", (double) totalStmts / totalProjects);
        metrics.put("AvgNom", (double) totalNom / totalProjects);
        metrics.put("AvgWac", (double) totalWac / totalProjects);
        metrics.put("AvgNocc", (double) totalNocc / totalProjects);
        metrics.put("AvgDit", (double) totalDit / totalProjects);
        metrics.put("AvgWmpc1", totalWmpc1 / totalProjects);
        metrics.put("AvgWmpc2", (double) totalWmpc2 / totalProjects);
        metrics.put("AvgRfc", (double) totalRfc / totalProjects);
        metrics.put("AvgCbo", (double) totalCbo / totalProjects);
        metrics.put("AvgMpc", (double) totalMpc / totalProjects);
        metrics.put("AvgLcom", (double) totalLcom / totalProjects);
        metrics.put("totalDependencies", totalDependencies);

        return metrics;
    }
}
