package uom.qualitydashboard.usersservice.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.usersservice.clients.OrganizationMicroserviceClient;
import uom.qualitydashboard.usersservice.models.CreateUserRequest;
import uom.qualitydashboard.usersservice.models.OrganizationDTO;
import uom.qualitydashboard.usersservice.models.OrganizationUser;
import uom.qualitydashboard.usersservice.repos.OrganizationUserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationUserService {
    private final OrganizationUserRepository organizationUserRepository;
    private final OrganizationMicroserviceClient organizationMicroserviceClient;

    public OrganizationUser createUser(CreateUserRequest user) {
        Long orgID = user.getOrganizationId();
        String userEmail = user.getEmail();

        Optional<OrganizationDTO> organization = organizationMicroserviceClient.getOrganizationById(orgID);

        if (organization.isEmpty()) throw new EntityNotFoundException("Given Organization does not Exist");

        Optional<OrganizationUser> existingUser = organizationUserRepository.findByEmail(userEmail);

        if (existingUser.isPresent()) throw new EntityExistsException("User with given email already exists");

        OrganizationUser newUser = OrganizationUser.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .userType(user.getUserType())
                .organizationId(user.getOrganizationId())
                .build();

        return organizationUserRepository.save(newUser);
    }

    public Collection<OrganizationUser> getAllUsers() {
        return organizationUserRepository.findAll();
    }

    public Optional<OrganizationUser> getUserById(Long id) {
        return organizationUserRepository.findById(id);
    }

    public Optional<OrganizationUser> getUserByEmail(String email) {
        return organizationUserRepository.findByEmail(email);
    }

    public Collection<OrganizationUser> getAllUsersByOrganizationId(Long organizationId) {
        return organizationUserRepository.findAllByOrganizationId(organizationId);
    }
}
