package uom.qualitydashboard.projectsubmissionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients	// Enable Feign Clients, used to fix the null pointer exception error on the client interfaces
public class ProjectSubmissionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectSubmissionServiceApplication.class, args);
	}

}
