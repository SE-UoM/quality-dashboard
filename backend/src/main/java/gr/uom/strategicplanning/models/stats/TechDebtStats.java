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
import java.util.Map;
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

    @ElementCollection
    private Map<String, Integer> codeSmells = Map.of(
            "MAJOR", 0,
            "MINOR", 0,
            "CRITICAL", 0,
            "BLOCKER", 0
    );

    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public TechDebtStats(OrganizationAnalysis organizationAnalysis) {
        this.organizationAnalysis = organizationAnalysis;
    }

    public Optional<Integer> updateCodeSmellDistribution(String key, int value) {
        if (codeSmells.containsKey(key)) {
            codeSmells.put(key, value);
            return Optional.of(value);
        }
        return Optional.empty();
    }

    public void initCodeSmellsDistribution() {
        codeSmells.put("MAJOR", 0);
        codeSmells.put("MINOR", 0);
        codeSmells.put("CRITICAL", 0);
        codeSmells.put("BLOCKER", 0);
        codeSmells.put("INFO", 0);
    }
}
