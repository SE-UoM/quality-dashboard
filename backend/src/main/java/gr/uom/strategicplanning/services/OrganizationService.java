package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public void saveOrganization(Organization organization) {
        organizationRepository.save(organization);
    }
}
