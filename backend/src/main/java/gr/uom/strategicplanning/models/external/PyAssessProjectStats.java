package gr.uom.strategicplanning.models.external;

import gr.uom.strategicplanning.models.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PyAssessProjectStats {

    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    private Set<String> dependencies = new HashSet<>();
    private Integer totalDependenencies;
    private Integer totalCoverage;
    private Integer totalMiss;
    private Integer totalStmts;

    private Integer nom;
    private Integer wac;
    private Integer nocc;
    private Integer dit;
    private Double wmpc1;
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

    public boolean addDependency(String dependency){
        if (dependencies.contains(dependency)) return false;
        return dependencies.add(dependency);
    }

}
