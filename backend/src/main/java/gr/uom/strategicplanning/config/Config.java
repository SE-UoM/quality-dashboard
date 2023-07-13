package gr.uom.strategicplanning.config;

import gr.uom.strategicplanning.models.User;
import gr.uom.strategicplanning.repositories.UserRepository;
import gr.uom.strategicplanning.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, UserService userService){
        return args -> {
            Optional<User> userOptional = userRepository.findByEmail("admin@uom.gr");
            if(!userOptional.isPresent()){
                User admin = new User(1L, "admin", "admin@uom.gr", "admin", "admin", true, null);
                userService.createUser(admin);
            }
        };
    }
}
