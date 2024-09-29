package gr.uom.strategicplanning.models.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Commit {
    @Id
    @GeneratedValue
    private Long id;
    private String hash;
    private Date commitDate;
    private double technicalDebt;
    private int totalFiles;
    private int totalLoC;
    private int totalCodeSmells;
    private double techDebtPerLoC;
    private double dmmUnitSize;
    private double dmmComplexity;
    private double dmmInterfacing;
    private String  maintainabilityRating = "UNKNOWN";

    @OneToMany
    private Collection<CodeSmell> codeSmells;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Developer developer;
    @ManyToOne
    private Project project;
    @OneToMany
    private List<RefactoringModel> refactoringModels;

    public void setCodeSmells(Collection<CodeSmell> codeSmells) {
        this.codeSmells = codeSmells;
        this.totalCodeSmells = codeSmells.size();
    }

    public void calculateTechDebtPerLoC() {
        this.techDebtPerLoC = this.technicalDebt / this.totalLoC;
    }
}
