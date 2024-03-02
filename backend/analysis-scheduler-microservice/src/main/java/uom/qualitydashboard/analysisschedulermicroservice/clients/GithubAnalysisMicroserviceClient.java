package uom.qualitydashboard.analysisschedulermicroservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uom.qualitydashboard.analysisschedulermicroservice.models.GithubAnalysisDTO;

import java.util.Collection;

@FeignClient(name = "github-analysis-microservice", url = "http://localhost:8088/api/v1/github-analysis/")
public interface GithubAnalysisMicroserviceClient {
    @PostMapping("/start")
    GithubAnalysisDTO startAnalysis(@RequestParam Long projectId);

    @GetMapping("project/id/{projectId}")
    Collection<GithubAnalysisDTO> getAnalysisByProjectId(@PathVariable Long projectId);


}
