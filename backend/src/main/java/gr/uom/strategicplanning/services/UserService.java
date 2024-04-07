package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.controllers.requests.ResetPasswordRequest;
import gr.uom.strategicplanning.controllers.requests.UserRegistrationRequest;
import gr.uom.strategicplanning.controllers.responses.implementations.UserResponse;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import gr.uom.strategicplanning.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Value("${frontend.url}")
    private String frontEndURL;
    private final int VERIFICATION_CODE_LENGTH = 150;

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

    @Autowired
    MailSendingService mailSendingService;

    SecureRandom secureRandom = new SecureRandom();

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

        String verificationCode = generateCode(VERIFICATION_CODE_LENGTH);
        user.setVerificationCode(verificationCode);

        User savedUser = userRepository.save(user);

        mailSendingService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getVerificationCode(),
                savedUser.getId(),
                frontEndURL
        );

        return savedUser;
    }

    public User verifyUser(String token, Long uid) {
        Optional<User> userOptional = userRepository.findById(uid);
        boolean userNotFound = userOptional.isEmpty();

        if(userNotFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification request for user not found");

        User user = userOptional.get();

        // encode the token
        token = UriUtils.encode(token, "UTF-8");
        System.out.println("Given token: " + token);
        System.out.println("User token: " + user.getVerificationCode());

        boolean verificationCodeExpired = user.verificationCodeExpired();
        if(verificationCodeExpired) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Verification has expired. Please request a new verification email.");

        boolean verificationIsValid = user.verificationIsValid(token);

        System.out.println("Verification is valid: " + verificationIsValid);
        if(!verificationIsValid) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid verification token");

        user.setVerified(true);
        user.setVerificationCode(null);
        return userRepository.save(user);
    }

    public User resendVerification(Long uid) {
        Optional<User> userOptional = userRepository.findById(uid);
        boolean userNotFound = userOptional.isEmpty();

        if(userNotFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification request for user not found");

        User user = userOptional.get();
        String verificationCode = generateCode(VERIFICATION_CODE_LENGTH);
        user.setVerificationCode(verificationCode);

        userRepository.save(user);
        mailSendingService.sendVerificationEmail(
                user.getEmail(),
                user.getVerificationCode(),
                user.getId(),
                frontEndURL
        );

        return user;
    }

    public void resetPasswordRequest(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        boolean userNotFound = userOptional.isEmpty();

        if(userNotFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + userEmail + " not found.");

        User user = userOptional.get();
        String passwordResetCode = generateCode(VERIFICATION_CODE_LENGTH);
        user.setPassResetCode(passwordResetCode);

        userRepository.save(user);

        mailSendingService.sendPasswordResetEmail(
                user.getEmail(),
                user.getPassResetCode(),
                user.getId(),
                frontEndURL
        );
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

    private String generateCode(int length) {
        byte[] buffer = new byte[length];
        secureRandom.nextBytes(buffer);

        String encodedBuffer = Base64.getEncoder().encodeToString(buffer);
        String code = encodedBuffer.substring(0, length);

        // Url Encode the code
        return UriUtils.encode(code, "UTF-8");
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String token = resetPasswordRequest.getToken();
        String newPassword = resetPasswordRequest.getPassword();
        Long uid = resetPasswordRequest.getUid();

        Optional<User> userOptional = userRepository.findById(uid);
        boolean userNotFound = userOptional.isEmpty();

        if(userNotFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Password Reset request for user not found");

        User user = userOptional.get();

        boolean passResetCodeExpired = user.passResetCodeExpired();
        if(passResetCodeExpired) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password Reset has expired. Please request a new password reset email.");

        boolean passResetCodeIsValid = user.passResetCodeIsValid(token);
        if(!passResetCodeIsValid) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid password reset token");

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPassResetCode(null);
        userRepository.save(user);
    }
}
