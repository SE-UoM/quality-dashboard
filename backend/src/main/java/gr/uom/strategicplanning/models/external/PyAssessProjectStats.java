package gr.uom.strategicplanning.models.external;

import gr.uom.strategicplanning.models.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PyAssessProjectStats {

    @Id
    @GeneratedValue
    private Long id;

    private String gitUrl;
    @ElementCollection
    private List<String> dependencies;
    private Integer totalCoverage;
    private Integer totalMiss;
    private Integer totalStmts;

    @OneToOne
    private Project project;

    public PyAssessProjectStats(Project project){
        this.project = project;
    }

}
