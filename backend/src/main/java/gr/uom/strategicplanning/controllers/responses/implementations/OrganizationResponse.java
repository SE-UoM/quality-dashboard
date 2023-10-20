package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.models.domain.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrganizationResponse {
    private Long id;
    private String organizationName;
    private List<ProjectResponse> projects;
    private OrganizationAnalysisResponse organizationAnalysis;
    
    
    public OrganizationResponse(Organization organization) {
        this.id = organization.getId();
        this.organizationName = organization.getName();
        this.projects = ProjectResponse.convertToProjectResponseList(organization.getProjects());

        if (organization.getOrganizationAnalysis() != null)
            this.organizationAnalysis = new OrganizationAnalysisResponse(organization.getOrganizationAnalysis());
    }

}
