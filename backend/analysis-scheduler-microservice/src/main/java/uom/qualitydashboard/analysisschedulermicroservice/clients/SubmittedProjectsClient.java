package uom.qualitydashboard.analysisschedulermicroservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(name = "submitted-projects-microservice", url = "http://localhost:8088/api/v1/projects/submissions")
public interface SubmittedProjectsClient {

}
