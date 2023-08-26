package gr.uom.strategicplanning.models.domain;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import lombok.*;

import javax.persistence.*;
import java.util.*;

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
    @ToString.Exclude
    private Organization organization;
    private String repoUrl;
    private int forks;
    private int stars;
    @OneToMany(mappedBy = "project")
    private Collection<Commit> commits = new ArrayList<>();
    private int totalDevelopers;
    private int totalCommits;
    @OneToMany(mappedBy = "project")
    private Collection<LanguageStats> languages = new ArrayList<>();
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

    public void addDeveloper(Developer developer) {
        this.developers.add(developer);
        developer.setProject(this);
    }
}
