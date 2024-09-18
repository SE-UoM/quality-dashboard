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

    private Integer nom;
    private Integer wac;
    private Integer nocc;
    private Integer dit;
    private Integer wmpc1;
    private Integer wmpc2;
    private Integer rfc;
    private Integer cbo;
    private Integer mpc;
    private Integer lcom;

    @OneToOne
    private Project project;

    public PyAssessProjectStats(Project project){
        this.project = project;
    }

}
