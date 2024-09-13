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

    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public PyAssessStats(OrganizationAnalysis organizationAnalysis){
        this.organizationAnalysis = organizationAnalysis;
    }

}
