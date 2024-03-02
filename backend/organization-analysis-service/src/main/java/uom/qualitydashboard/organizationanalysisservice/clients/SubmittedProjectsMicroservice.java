package uom.qualitydashboard.organizationanalysisservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uom.qualitydashboard.organizationanalysisservice.models.SubmittedProjectDTO;

import java.util.Collection;

@FeignClient(name = "submitted-projects-microservice", url = "http://localhost:8088/api/v1/projects/submissions")
public interface SubmittedProjectsMicroservice {
    @GetMapping("/organization/{organizationId}")
    Collection<SubmittedProjectDTO> getSubmittedProjectsByOrganizationId(@PathVariable Long organizationId);
}
