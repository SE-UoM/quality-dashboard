package uom.qualitydashboard.organizationanalysisservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uom.qualitydashboard.organizationanalysisservice.models.GithubAnalysisDTO;

import java.util.Optional;

@FeignClient(name = "github-analysis-microservice", url = "http://localhost:8089/api/v1/github-analysis")
public interface GithubAnalysisMicroserviceClient {
    @GetMapping("/project/latest/{projectId}")
    Optional<GithubAnalysisDTO> getLatestProjectAnalysis(@PathVariable Long projectId);
}
