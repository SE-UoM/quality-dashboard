package uom.qualitydashboard.organizationservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.organizationservice.models.Organization;
import uom.qualitydashboard.organizationservice.repos.OrganizationRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public Organization saveOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Collection<Organization> getOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> getOrganizationById(Long organizationId) {
        return organizationRepository.findById(organizationId);
    }
}
