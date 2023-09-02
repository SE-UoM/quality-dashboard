package gr.uom.strategicplanning.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uom.strategicplanning.controllers.requests.UserRegistrationRequest;
import gr.uom.strategicplanning.models.domain.BestPractice;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.BestPracticesRepository;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.repositories.UserRepository;
import gr.uom.strategicplanning.services.UserPrivilegedService;
import gr.uom.strategicplanning.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

@Configuration
@EnableSwagger2
public class Config {

    @Value("${superuser.organization}")
    private String superuserOrganization;

    @Value("${superuser.email}")
    private String superuserEmail;

    @Value("${superuser.password}")
    private String superuserPassword;

    @Value("${superuser.name}")
    private String superuserName;

    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository, UserService userService,
            UserPrivilegedService userPrivilegedService, OrganizationRepository organizationRepository,
            BestPracticesRepository bestPracticesRepository){
        return args -> {
            // Load superuser and organization
            Optional<Organization> organization = organizationRepository.findByName(superuserOrganization);
            if (!organization.isPresent()) {
                Optional<User> userOptional = userRepository.findByEmail(superuserEmail);
                if(!userOptional.isPresent()){
                    User admin = new User(superuserName, superuserEmail, superuserPassword);
                    Organization organization1 = userService.createOrganization(superuserOrganization, admin);

                    UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
                    registrationRequest.setEmail(superuserEmail);
                    registrationRequest.setName(superuserName);
                    registrationRequest.setPassword(superuserPassword);
                    registrationRequest.setOrganizationId(organization1.getId());

                    userService.createUser(registrationRequest);
                    userPrivilegedService.verifyUser(superuserEmail);
                    userPrivilegedService.givePrivilegeToUser(superuserEmail);
                }
            }

            // Load best practices
            ObjectMapper objectMapper = new ObjectMapper();
            BestPractice bestPractices[] = objectMapper.readValue(Config.class.getResourceAsStream("/best_practices.json"), BestPractice[].class);

            for (BestPractice bestPractice : bestPractices) {
                bestPracticesRepository.save(bestPractice);
            }
        };
    }
}
