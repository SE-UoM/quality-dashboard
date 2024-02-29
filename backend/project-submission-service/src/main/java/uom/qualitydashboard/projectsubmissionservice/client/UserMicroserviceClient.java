package uom.qualitydashboard.projectsubmissionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uom.qualitydashboard.projectsubmissionservice.models.User;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://localhost:8088/api/v1/users")
public interface UserMicroserviceClient {
    @GetMapping("/id/{userId}")
    Optional<User> getUserById(@PathVariable Long userId);
}
