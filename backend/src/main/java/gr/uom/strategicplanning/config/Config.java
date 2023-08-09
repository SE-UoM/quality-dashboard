package gr.uom.strategicplanning.config;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.repositories.UserRepository;
import gr.uom.strategicplanning.services.UserPrivilegedService;
import gr.uom.strategicplanning.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
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
    CommandLineRunner commandLineRunner(UserRepository userRepository, UserService userService, UserPrivilegedService userPrivilegedService
    , OrganizationRepository organizationRepository){
        return args -> {
            Optional<Organization> organization = organizationRepository.findByName("University of Macedonia");
            if (!organization.isPresent()) {
                Optional<User> userOptional = userRepository.findByEmail("admin@uom.gr");
                if(!userOptional.isPresent()){
                    User admin = new User(superuserName, superuserEmail, superuserPassword);
                    userService.createOrganization(superuserOrganization, admin);
                    userService.createUser(admin);
                    userPrivilegedService.verifyUser(superuserEmail);
                    userPrivilegedService.givePrivilegeToUser(superuserEmail);
                }
            }


        };
    }
}
