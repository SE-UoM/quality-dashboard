package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.external.PyAssessProjectStats;
import gr.uom.strategicplanning.models.stats.PyAssessStats;
import gr.uom.strategicplanning.repositories.PyAssessProjectStatsRepository;
import gr.uom.strategicplanning.repositories.PyAssessStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PyAssessService {

    @Autowired
    private PyAssessProjectStatsRepository pyAssessProjectStatsRepository;
    @Autowired
    private PyAssessStatsRepository pyAssessStatsRepository;

    public void populatePyAssessModel(ResponseEntity response) {

        Object body = response.getBody();
        if (body != null && body instanceof Map) {
            Map<String, Object> bodyMap = (Map<String, Object>) body;
            String gitUrl = (String) bodyMap.get("gitUrl");
            if (gitUrl != null) {

                Optional<PyAssessProjectStats> pyAssessProjectStatsOptional = pyAssessProjectStatsRepository.findByGitUrl(gitUrl);
                PyAssessProjectStats pyAssessProjectStats;
                pyAssessProjectStats = pyAssessProjectStatsOptional.orElseGet(PyAssessProjectStats::new);
                populateModels(bodyMap, pyAssessProjectStats);
                populateOrganizationalStats(pyAssessProjectStats);
            } else {
                // there was a problem with the response
            }
        } else {
            System.out.println("Response body is null or not a Map");
        }

    }

    private void populateModels(Map<String, Object> bodyMap, PyAssessProjectStats pyAssessProjectStats) {
        pyAssessProjectStats.setGitUrl((String) bodyMap.get("gitUrl"));

        List<Map<String, Object>> analyzedProjects = (List<Map<String, Object>>) bodyMap.get("singleAnalyzedProjectList");
        if (analyzedProjects != null && !analyzedProjects.isEmpty()) {
            Map<String, Object> projectDetails = analyzedProjects.get(0); // Assuming you want the first one
            pyAssessProjectStats.setDependencies((List<String>) projectDetails.get("dependencies"));
            pyAssessProjectStats.setTotalCoverage((Integer) projectDetails.get("totalCoverage"));
            pyAssessProjectStats.setTotalMiss((Integer) projectDetails.get("totalMiss"));
            pyAssessProjectStats.setTotalStmts((Integer) projectDetails.get("totalStmts"));

            // Extract project quality metrics if available
            Map<String, Object> projectQualityMetrics = (Map<String, Object>) projectDetails.get("projectQualityMetrics");
            if (projectQualityMetrics != null) {
                pyAssessProjectStats.setNom((Integer) projectQualityMetrics.get("nom"));
                pyAssessProjectStats.setWac((Integer) projectQualityMetrics.get("wac"));
                pyAssessProjectStats.setNocc((Integer) projectQualityMetrics.get("nocc"));
                pyAssessProjectStats.setDit((Integer) projectQualityMetrics.get("dit"));
                pyAssessProjectStats.setWmpc1((Integer) projectQualityMetrics.get("wmpc1"));
                pyAssessProjectStats.setWmpc2((Integer) projectQualityMetrics.get("wmpc2"));
                pyAssessProjectStats.setRfc((Integer) projectQualityMetrics.get("rfc"));
                pyAssessProjectStats.setCbo((Integer) projectQualityMetrics.get("cbo"));
                pyAssessProjectStats.setMpc((Integer) projectQualityMetrics.get("mpc"));
                pyAssessProjectStats.setLcom((Integer) projectQualityMetrics.get("lcom"));
            }
        }

        pyAssessProjectStatsRepository.save(pyAssessProjectStats);
    }

    private void populateOrganizationalStats(PyAssessProjectStats pyAssessProjectStats) {
        Optional<PyAssessStats> pyAssessStatsOptional = pyAssessStatsRepository.existsInGitUrls(pyAssessProjectStats.getGitUrl());
        if(pyAssessStatsOptional.isPresent()){
            PyAssessStats pyAssessStats = pyAssessStatsOptional.get();
            pyAssessStats.getGitUrls().add(pyAssessProjectStats.getGitUrl());
            pyAssessStats.getDependencies().addAll(pyAssessProjectStats.getDependencies());
            pyAssessStats.setAverageCoverage((pyAssessStats.getAverageCoverage() + pyAssessProjectStats.getTotalCoverage()) / 2);
            pyAssessStats.setAverageMiss((pyAssessStats.getAverageMiss() + pyAssessProjectStats.getTotalMiss()) / 2);
            pyAssessStats.setAverageStmts((pyAssessStats.getAverageStmts() + pyAssessProjectStats.getTotalStmts()) / 2);
            pyAssessStats.setAverageNom((pyAssessStats.getAverageNom() + pyAssessProjectStats.getNom()) / 2);
            pyAssessStats.setAverageWac((pyAssessStats.getAverageWac() + pyAssessProjectStats.getWac()) / 2);
            pyAssessStats.setAverageNocc((pyAssessStats.getAverageNocc() + pyAssessProjectStats.getNocc()) / 2);
            pyAssessStats.setAverageDit((pyAssessStats.getAverageDit() + pyAssessProjectStats.getDit()) / 2);
            pyAssessStats.setAverageWmpc1((pyAssessStats.getAverageWmpc1() + pyAssessProjectStats.getWmpc1()) / 2);
            pyAssessStats.setAverageWmpc2((pyAssessStats.getAverageWmpc2() + pyAssessProjectStats.getWmpc2()) / 2);
            pyAssessStats.setAverageRfc((pyAssessStats.getAverageRfc() + pyAssessProjectStats.getRfc()) / 2);
            pyAssessStats.setAverageCbo((pyAssessStats.getAverageCbo() + pyAssessProjectStats.getCbo()) / 2);
            pyAssessStats.setAverageMpc((pyAssessStats.getAverageMpc() + pyAssessProjectStats.getMpc()) / 2);
            pyAssessStats.setAverageLcom((pyAssessStats.getAverageLcom() + pyAssessProjectStats.getLcom()) / 2);
            pyAssessStatsRepository.save(pyAssessStats);
        } else {
            PyAssessStats pyAssessStats = new PyAssessStats();
            pyAssessStats.getGitUrls().add(pyAssessProjectStats.getGitUrl());
            pyAssessStats.getDependencies().addAll(pyAssessProjectStats.getDependencies());
            pyAssessStats.setAverageCoverage(Long.valueOf(pyAssessProjectStats.getTotalCoverage()));
            pyAssessStats.setAverageMiss(Long.valueOf(pyAssessProjectStats.getTotalMiss()));
            pyAssessStats.setAverageStmts(Long.valueOf(pyAssessProjectStats.getTotalStmts()));
            pyAssessStats.setAverageNom(Long.valueOf(pyAssessProjectStats.getNom()));
            pyAssessStats.setAverageWac(Long.valueOf(pyAssessProjectStats.getWac()));
            pyAssessStats.setAverageNocc(Long.valueOf(pyAssessProjectStats.getNocc()));
            pyAssessStats.setAverageDit(Long.valueOf(pyAssessProjectStats.getDit()));
            pyAssessStats.setAverageWmpc1(Long.valueOf(pyAssessProjectStats.getWmpc1()));
            pyAssessStats.setAverageWmpc2(Long.valueOf(pyAssessProjectStats.getWmpc2()));
            pyAssessStats.setAverageRfc(Long.valueOf(pyAssessProjectStats.getRfc()));
            pyAssessStats.setAverageCbo(Long.valueOf(pyAssessProjectStats.getCbo()));
            pyAssessStats.setAverageMpc(Long.valueOf(pyAssessProjectStats.getMpc()));
            pyAssessStats.setAverageLcom(Long.valueOf(pyAssessProjectStats.getLcom()));
            pyAssessStatsRepository.save(pyAssessStats);
        }
    }

}
