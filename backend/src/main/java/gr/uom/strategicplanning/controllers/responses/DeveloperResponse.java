package gr.uom.strategicplanning.controllers.responses;

import gr.uom.strategicplanning.models.domain.Developer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeveloperResponse {
    public Long id;
    public String name;
    public String githubUrl;
    public int totalCommits;
    public Long projectId;

    public DeveloperResponse(Developer developer) {
        this.id = developer.getId();
        this.name = developer.getName();
        this.githubUrl = developer.getGithubUrl();
        this.totalCommits = developer.getTotalCommits();
        this.projectId = developer.getProject().getId();
    }
}