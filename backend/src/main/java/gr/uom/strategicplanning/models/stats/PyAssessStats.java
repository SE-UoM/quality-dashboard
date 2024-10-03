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
    private Double totalCoverage;
    private Double totalMiss;
    private Double totalStmts;

    private Double averageNom;
    private Double averageWac;
    private Double averageNocc;
    private Double averageDit;
    private Double averageWmpc1;
    private Double averageWmpc2;
    private Double averageRfc;
    private Double averageCbo;
    private Double averageMpc;
    private Double averageLcom;
    private Integer totalDependencies;

    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public PyAssessStats(OrganizationAnalysis organizationAnalysis){
        this.organizationAnalysis = organizationAnalysis;
    }

    public boolean addGitUrl(String gitUrl){
        return gitUrls.add(gitUrl);
    }

    public boolean addDependency(String dependency){
        return dependencies.add(dependency);
    }

    public boolean addMultipleGitUrls(Set<String> gitUrls){
        return this.gitUrls.addAll(gitUrls);
    }

    public boolean addMultipleDependencies(Set<String> dependencies){
        return this.dependencies.addAll(dependencies);
    }
}
