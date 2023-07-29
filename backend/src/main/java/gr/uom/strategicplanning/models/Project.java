package gr.uom.strategicplanning.models;

import gr.uom.strategicplanning.enums.ProjectStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private Organization organization;
    private String repoUrl;
    private int forks;
    private int stars;
    @OneToMany(mappedBy = "project")
    private Collection<Commit> commits = new ArrayList<>();
    private int totalDevelopers;
    private int totalCommits;
    @OneToMany(mappedBy = "project")
    private Collection<Language> languages = new ArrayList<>();
    private int totalLanguages;
    @OneToMany(mappedBy = "project")
    private Set<Developer> developers = new HashSet<>();
    private ProjectStatus status = ProjectStatus.ANALYSIS_NOT_STARTED;

    @OneToOne(mappedBy = "project")
    private ProjectStats projectStats;

    public boolean canBeAnalyzed() {
        if (this.totalCommits >= OrganizationAnalysis.COMMITS_THRESHOLD) {
            this.status = ProjectStatus.ANALYSIS_TO_BE_REVIEWED;
            return false;
        }

        this.status = ProjectStatus.ANALYSIS_NOT_STARTED;
        return true;
    }

    public void addCommit(Commit commit) {
        this.commits.add(commit);
        commit.setProject(this);
    }
}
