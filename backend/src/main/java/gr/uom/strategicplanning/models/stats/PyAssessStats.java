package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PyAssessStats {

    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    private Set<String> gitUrls;
    @ElementCollection
    private Set<String> dependencies;
    private Long averageCoverage;
    private Long averageMiss;
    private Long averageStmts;

    private Long averageNom;
    private Long averageWac;
    private Long averageNocc;
    private Long averageDit;
    private Long averageWmpc1;
    private Long averageWmpc2;
    private Long averageRfc;
    private Long averageCbo;
    private Long averageMpc;
    private Long averageLcom;

    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public PyAssessStats(OrganizationAnalysis organizationAnalysis){
        this.organizationAnalysis = organizationAnalysis;
    }

}
