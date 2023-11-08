package gr.uom.strategicplanning.models.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uom.strategicplanning.models.domain.ProjectCodeSmellDistribution;
import gr.uom.strategicplanning.models.domain.Project;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectStats {
    @Id
    @GeneratedValue
    private Long id;
    private int totalLoC;
    private int totalFiles;
    private int totalCodeSmells;
    private int techDebt;
    private double techDebtPerLoC;

    @OneToMany(mappedBy = "projectStats", cascade = CascadeType.PERSIST)
    private Collection<ProjectCodeSmellDistribution> codeSmellDistributions;

    @OneToOne
    @ToString.Exclude
    @JsonIgnore
    private Project project;

    public ProjectStats(Project project) {
        this.project = project;
    }

    public void calculateTechDebtPerLoC() {
        this.techDebtPerLoC = this.techDebt/ this.totalLoC;
    }
}
