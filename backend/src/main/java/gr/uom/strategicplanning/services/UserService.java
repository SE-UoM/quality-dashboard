package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.controllers.requests.UserRegistrationRequest;
import gr.uom.strategicplanning.controllers.responses.implementations.UserResponse;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    OrganizationAnalysisService organizationAnalysisService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(UserRegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        String password = registrationRequest.getPassword();
        String name = registrationRequest.getName();
        Long orgID = registrationRequest.getOrganizationId();

        Optional<User> userOptional = userRepository.findByEmail(email);
        Optional<Organization> organizationOptional = organizationRepository.findById(orgID);

        if(userOptional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The email is already in use");
        if(organizationOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setOrganization(organizationOptional.get());
        user.setRoles("SIMPLE");

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Organization createOrganization(String name, User admin) {
        Organization organization = new Organization();
        organization.setName(name);
        organization.addUser(admin);
        admin.setOrganization(organization);
        organizationAnalysisService.saveOrganizationAnalysis(organization.getOrganizationAnalysis());
        return organizationRepository.save(organization);
    }

    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        boolean userNotFound = userOptional.isEmpty();

        if(userNotFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        return userOptional.get();
    }

    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        boolean userNotFound = userOptional.isEmpty();

        if(userNotFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        return userOptional.get();
    }

    public Optional<List<UserResponse>> getUsersByOrganizationId(Long id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        boolean organizationNotFound = organizationOptional.isEmpty();

        if(organizationNotFound) return Optional.empty();

        Organization organization = organizationOptional.get();
        List<User> users = organization.getUsers();
        List<UserResponse> userResponses = UserResponse.convertToUserResponseList(users);
        return Optional.of(userResponses);
    }
}
