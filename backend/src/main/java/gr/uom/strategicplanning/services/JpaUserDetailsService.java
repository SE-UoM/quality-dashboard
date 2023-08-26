package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.users.SecurityUser;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional= userRepository.findByEmail(username);

        if(userOptional.isEmpty()) throw new UsernameNotFoundException("Email not found: " + username);

        return new SecurityUser(userOptional.get());
    }
}