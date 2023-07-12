package gr.uom.strategicplanning.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

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
    @OneToOne
    private Language minDebtLanguage;
    @OneToMany
    private Collection<Project> bestTechDebtProjects;
    @OneToMany
    private Collection<Project> bestCodeSmellProjects;
    private int totalCodeSmells;
    @OneToMany
    private Collection<CodeSmell> codeSmells;

}
