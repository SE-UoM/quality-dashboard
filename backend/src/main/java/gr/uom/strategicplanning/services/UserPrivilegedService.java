package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class UserPrivilegedService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public User verifyUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with email "+ email +" doesn't exist!"
        ));
        user.setVerified(true);

        userRepository.save(user);
        return user;
    }

    @Transactional
    public User givePrivilegeToUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with email "+ email +" doesn't exist!"
        ));
        String roles=user.getRoles();
        if(roles.equals("SIMPLE,PRIVILEGED")){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This user is Privileged");
        }
        user.setRoles(roles+",PRIVILEGED");

        userRepository.save(user);
        return user;
    }
}
