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
    private String ownerName;

    @ManyToOne
    @ToString.Exclude
    private Organization organization;
    private String repoUrl;
    private int forks;
    private int stars;
    @OneToMany(mappedBy = "project")
    private Collection<Commit> commits = new ArrayList<>();
    private int totalDevelopers = 0;
    private int totalCommits;
    @OneToMany(mappedBy = "project")
    private Collection<ProjectLanguage> languages = new ArrayList<>();
    private int totalLanguages;
    private int totalRefactorings;

    @ManyToMany()
    @JoinTable(
            name = "project_developer",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id"))
    private Set<Developer> developers = new HashSet<>();

    private ProjectStatus status = ProjectStatus.ANALYSIS_NOT_STARTED;
    private ProjectStatus codeInspectorStatus = ProjectStatus.ANALYSIS_NOT_STARTED;
    private ProjectStatus pyAssessStatus = ProjectStatus.ANALYSIS_NOT_STARTED;

    @OneToOne(mappedBy = "project", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private ProjectStats projectStats = new ProjectStats(this);
    
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
        if (!developerExists(developer)) {
            this.developers.add(developer);
            developer.getProjects().add(this);
        }
    }

    public boolean hasLanguage(String language) {
        for (ProjectLanguage projectLanguage : this.languages) {
            String languageName = projectLanguage.getName();
            if (languageName.equalsIgnoreCase(language)) {
                return true;
            }
        }

        return false;
    }

    private boolean developerExists(Developer developer) {
        return this.developers.contains(developer);
    }
}
