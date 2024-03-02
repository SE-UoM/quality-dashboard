package uom.qualitydashboard.organizationanalysisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrganizationAnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizationAnalysisServiceApplication.class, args);
	}

}
