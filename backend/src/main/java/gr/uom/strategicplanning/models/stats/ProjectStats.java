package gr.uom.strategicplanning.models.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uom.strategicplanning.models.domain.CodeSmellDistribution;
import gr.uom.strategicplanning.models.domain.Project;
import io.swagger.models.auth.In;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;

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
    private Collection<CodeSmellDistribution> codeSmellDistributions;

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
