package gr.uom.strategicplanning.config;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.repositories.UserRepository;
import gr.uom.strategicplanning.services.UserPrivilegedService;
import gr.uom.strategicplanning.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, UserService userService, UserPrivilegedService userPrivilegedService
    , OrganizationRepository organizationRepository){
        return args -> {
            Optional<Organization> organization = organizationRepository.findByName("University of Macedonia");
            if (!organization.isPresent()) {
                Optional<User> userOptional = userRepository.findByEmail("admin@uom.gr");
                if(!userOptional.isPresent()){
                    User admin = new User("admin", "admin@uom.gr", "admin");
                    userService.createOrganization("University of Macedonia", admin);
                    userService.createUser(admin);
                    userPrivilegedService.verifyUser("admin@uom.gr");
                    userPrivilegedService.givePrivilegeToUser("admin@uom.gr");
                }
            }


        };
    }
}
