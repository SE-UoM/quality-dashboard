package gr.uom.strategicplanning.models.domain;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Developer {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String githubUrl;
    private String avatarUrl = GithubApiClient.DEFAULT_AVATAR_URL;
    private int totalCommits;
    private int totalCodeSmells;
    private double codeSmellsPerCommit;

    @ManyToMany(mappedBy = "developers")
    private Set<Project> projects = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Developer))
            return false;
        if (obj == this)
            return true;
        return this.getName().equals(((Developer) obj).getName());
    }
}
