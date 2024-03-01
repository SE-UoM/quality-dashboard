package uom.qualitydashboard.githubanalysisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GithubAnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubAnalysisServiceApplication.class, args);
	}

}
