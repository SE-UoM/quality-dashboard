package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.models.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SimpleProjectResponse implements ResponseInterface {
    private Long id;
    private String name;
    private String organizationName;
    private Long organizationId;
    private String repoUrl;
    private String status;

    public SimpleProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.organizationName = project.getOrganization().getName();
        this.organizationId = project.getOrganization().getId();
        this.repoUrl = project.getRepoUrl();
        this.status = project.getStatus().name();
    }
}
