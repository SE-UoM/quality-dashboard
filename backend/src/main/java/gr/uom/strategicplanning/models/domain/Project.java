package gr.uom.strategicplanning.models.domain;

import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.external.CodeInspectorProjectStats;
import gr.uom.strategicplanning.models.external.PyAssessProjectStats;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    private String projectDescription;
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

    @OneToOne(cascade = CascadeType.PERSIST)
            private CodeInspectorProjectStats codeInspectorProjectStats = new CodeInspectorProjectStats(this);
    @OneToOne(cascade = CascadeType.PERSIST)
    private PyAssessProjectStats pyAssessProjectStats = new PyAssessProjectStats(this);

    private PyAssessProjectStats checkForPythonGitRepo() {
        for (ProjectLanguage projectLanguage : this.languages) {
            if (projectLanguage.getName().equalsIgnoreCase("Python")) {
                return new PyAssessProjectStats(this);
            }
        }

        return null;
    }

    private String defaultBranchName;

    private String mainLang;

    public void addCommit(Commit commit) {
        this.commits.add(commit);
        commit.setProject(this);
    }

    public boolean commitsAlreadyAnalyzed(String sha) {
        for (Commit commit : this.commits) {
            if (commit.getHash().equals(sha)) return true;
        }

        return false;
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

    public boolean hasLessCommitsThanThreshold() {
        return totalCommits < OrganizationAnalysis.COMMITS_THRESHOLD;
    }
}
