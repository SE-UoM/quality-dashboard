package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
    @OneToMany(cascade = CascadeType.PERSIST)
    private Collection<OrganizationCodeSmellDistribution> codeSmells = new ArrayList<>();
    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public TechDebtStats(OrganizationAnalysis organizationAnalysis) {
        this.organizationAnalysis = organizationAnalysis;
        initCodeSmellsDistribution();
    }

    private void initCodeSmellsDistribution() {
        OrganizationCodeSmellDistribution majorCodeSmell = new OrganizationCodeSmellDistribution();
        majorCodeSmell.setSeverity("Major");
        majorCodeSmell.setCount(0);
        majorCodeSmell.setTechDebtStats(this);

        OrganizationCodeSmellDistribution minorCodeSmell = new OrganizationCodeSmellDistribution();
        minorCodeSmell.setSeverity("Minor");
        minorCodeSmell.setCount(0);
        minorCodeSmell.setTechDebtStats(this);

        OrganizationCodeSmellDistribution criticalCodeSmell = new OrganizationCodeSmellDistribution();
        criticalCodeSmell.setSeverity("Critical");
        criticalCodeSmell.setCount(0);
        criticalCodeSmell.setTechDebtStats(this);

        OrganizationCodeSmellDistribution blockerCodeSmell = new OrganizationCodeSmellDistribution();
        blockerCodeSmell.setSeverity("Blocker");
        blockerCodeSmell.setCount(0);
        blockerCodeSmell.setTechDebtStats(this);

        OrganizationCodeSmellDistribution infoCodeSmell = new OrganizationCodeSmellDistribution();
        infoCodeSmell.setSeverity("Info");
        infoCodeSmell.setCount(0);
        infoCodeSmell.setTechDebtStats(this);

        codeSmells.add(majorCodeSmell);
        codeSmells.add(minorCodeSmell);
        codeSmells.add(criticalCodeSmell);
        codeSmells.add(blockerCodeSmell);
        codeSmells.add(infoCodeSmell);
    }

    public Optional<OrganizationCodeSmellDistribution> getCodeSmellDistribution(String severity) {
        for (OrganizationCodeSmellDistribution codeSmellDistribution : codeSmells) {
            if (codeSmellDistribution.severityEquals(severity)) return Optional.of(codeSmellDistribution);
        }
        return Optional.empty();
    }

    public void resetCodeSmellDistribution() {
        for (OrganizationCodeSmellDistribution codeSmellDistribution : codeSmells) {
            codeSmellDistribution.setCount(0);
        }
    }
}
