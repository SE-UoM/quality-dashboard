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
    @OneToOne
    private Project ProjectWithMinTechDebt;
    @OneToOne
    private Project ProjectWithMaxTechDebt;
    private float averageTechDebtPerLoC;
    @OneToMany
    private Collection<Project> bestTechDebtProjects;
    @OneToMany
    private Collection<Project> bestCodeSmellProjects;
    private int totalCodeSmells;
    @OneToMany
    private Collection<CodeSmell> codeSmells;
    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

}
