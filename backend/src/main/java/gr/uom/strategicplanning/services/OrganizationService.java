package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public void saveOrganization(Organization organization) {
        organizationRepository.save(organization);
    }

    public Organization getOrganizationById(Long id) {
        Optional<Organization> organization = organizationRepository.findById(id);

        if(organization.isEmpty()) throw new EntityNotFoundException("Organization with id " + id + " not found");

        return organization.get();
    }

    public boolean organizationExistsById(Long id) {
        return organizationRepository.existsById(id);
    }

    public void addProjectToOrganization(Long organizationId, Project project) {
        Organization organization = getOrganizationById(organizationId);

        organization.addProject(project);

        saveOrganization(organization);
    }
}
