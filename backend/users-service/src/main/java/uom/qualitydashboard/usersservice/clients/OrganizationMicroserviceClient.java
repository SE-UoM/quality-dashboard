package uom.qualitydashboard.usersservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uom.qualitydashboard.usersservice.models.OrganizationDTO;

import java.util.Optional;

@FeignClient(name = "organization-service", url = "http://localhost:8088/api/v1/organization")
public interface OrganizationMicroserviceClient {
    @GetMapping("/id/{organizationId}")
    Optional<OrganizationDTO> getOrganizationById(@PathVariable Long organizationId);
}
