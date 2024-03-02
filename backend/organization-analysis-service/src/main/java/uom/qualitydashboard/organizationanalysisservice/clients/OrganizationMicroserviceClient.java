package uom.qualitydashboard.organizationanalysisservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "organization-microservice", url = "http://localhost:8088/api/v1/organization")
public interface OrganizationMicroserviceClient {
    @GetMapping("/id/{organizationId}")
    String getOrganizationById(@PathVariable Long organizationId);
}
