package uom.qualitydashboard.usersservice.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.usersservice.models.OrganizationUser;
import uom.qualitydashboard.usersservice.repos.OrganizationUserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationUserService {
    private final OrganizationUserRepository organizationUserRepository;

    public OrganizationUser saveUser(OrganizationUser user) {
        return organizationUserRepository.save(user);
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
