package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.User;
import gr.uom.strategicplanning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if(!userOptional.isPresent()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles("SIMPLE");
            user.setVerified(false);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email is used from another user");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
