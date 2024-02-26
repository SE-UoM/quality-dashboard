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

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Configuration
@EnableSwagger2
public class InitApplicationConfig {

    @Value("${superuser.organization}")
    private String superuserOrganization;

    @Value("${superuser.email}")
    private String superuserEmail;

    @Value("${superuser.password}")
    private String superuserPassword;

    @Value("${superuser.name}")
    private String superuserName;

    @Bean
    public CommandLineRunner initApplication(
            UserRepository userRepository, UserService userService,
            UserPrivilegedService userPrivilegedService, OrganizationRepository organizationRepository,
            BestPracticesRepository bestPracticesRepository
    ) {
        return args -> {
            createUser(userRepository, userService, userPrivilegedService, organizationRepository);
            loadBestPractices(bestPracticesRepository);
        };
    }

    private void createUser(
            UserRepository userRepository, UserService userService,
            UserPrivilegedService userPrivilegedService, OrganizationRepository organizationRepository
    ) {
        // Find or create organization
        Optional<Organization> organization = organizationRepository.findByName(superuserOrganization);
        Organization organizationToSave = organization.orElseGet(() ->
                userService.createOrganization(superuserOrganization, new User(superuserName, superuserEmail, superuserPassword)));

        // Check and create user if needed
        Optional<User> existingUser = userRepository.findByEmail(superuserEmail);
        if (!existingUser.isPresent()) {
            UserRegistrationRequest registrationRequest = new UserRegistrationRequest(superuserName, superuserEmail, superuserPassword, organizationToSave.getId());
            userService.createUser(registrationRequest);
            userPrivilegedService.verifyUser(superuserEmail);
            userPrivilegedService.givePrivilegeToUser(superuserEmail);
        }
    }

    private void loadBestPractices(BestPracticesRepository bestPracticesRepository) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BestPractice bestPractices[] = objectMapper.readValue(InitApplicationConfig.class.getResourceAsStream("/best_practices.json"), BestPractice[].class);

        Collection<BestPractice> bestPracticesCollection = bestPracticesRepository.findAll();

        if (bestPracticesCollection.size() < bestPractices.length) {
            for (BestPractice bestPractice : bestPractices) {
                String currentPracticeTitle = bestPractice.getTitle();
                Optional<BestPractice> bestPracticeFound = bestPracticesRepository.findByTitle(currentPracticeTitle);

                if (bestPracticeFound.isEmpty()) bestPracticesRepository.save(bestPractice);
            }
        }
    }
}
