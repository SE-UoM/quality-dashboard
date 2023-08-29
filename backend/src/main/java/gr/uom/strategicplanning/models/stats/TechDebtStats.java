package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.CodeSmell;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechDebtStats {
    
    @Id
    @GeneratedValue
    private Long id;
    private float totalTechDebt;
    private float averageTechDebt;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Project ProjectWithMinTechDebt;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Project ProjectWithMaxTechDebt;
    private float averageTechDebtPerLoC;
    @OneToMany(cascade = CascadeType.PERSIST)
    private Collection<Project> bestTechDebtProjects = new ArrayList<>();
    @OneToMany(cascade = CascadeType.PERSIST)
    private Collection<Project> bestCodeSmellProjects = new ArrayList<>();
    private int totalCodeSmells;
    @OneToMany
    private Collection<CodeSmell> codeSmells = new ArrayList<>();
    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public TechDebtStats(OrganizationAnalysis organizationAnalysis) {
        this.organizationAnalysis = organizationAnalysis;
    }
}
