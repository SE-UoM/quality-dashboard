package gr.uom.strategicplanning.controllers.responses;

import gr.uom.strategicplanning.models.domain.Developer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeveloperResponse {
    public Long id;
    public String name;
    public String githubUrl;
    public int totalCommits;
    public int totalCodeSmells;
    public double codeSmellsPerCommit;
    public Long projectId;

    public DeveloperResponse(Developer developer) {
        this.id = developer.getId();
        this.name = developer.getName();
        this.githubUrl = developer.getGithubUrl();
        this.totalCommits = developer.getTotalCommits();
        this.totalCodeSmells = developer.getTotalCodeSmells();
        this.codeSmellsPerCommit = developer.getCodeSmellsPerCommit();
        this.projectId = developer.getProject().getId();
    }

    public static Set<DeveloperResponse> convertToDeveloperResponseSet(Collection<Developer> developerCollection) {
        Set<DeveloperResponse> developerResponses = new java.util.HashSet<>();

        for (Developer developer : developerCollection) {
            developerResponses.add(new DeveloperResponse(developer));
        }

        return developerResponses;
    }
}