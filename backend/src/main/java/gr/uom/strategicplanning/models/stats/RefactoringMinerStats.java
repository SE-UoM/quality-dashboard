package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefactoringMinerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalDifferentRefactorings;
    private int totalRefactoringTypes;
//    private int totalRefactoringsInvolvingClasses;
//    private int totalRefactoringsInvolvingMethods;
    @ElementCollection
    private Set<String> refactoringTypes;
    @ElementCollection
    private Set<String> refactoringNames;
    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public RefactoringMinerStats(OrganizationAnalysis organizationAnalysis) {
        this.organizationAnalysis = organizationAnalysis;
    }

    public synchronized void incrementTotalDifferentRefactoringsBy(int increment) {
        this.totalDifferentRefactorings += increment;
    }

    public boolean addRefactoringType(String refactoringType) {
        return refactoringTypes.add(refactoringType);
    }

    public boolean addRefactoringName(String refactoringName) {
        return refactoringNames.add(refactoringName);
    }

    public boolean addMultipleRefactoringTypes(Set<String> refactoringTypes) {
        boolean add = this.refactoringTypes.addAll(refactoringTypes);
        this.totalRefactoringTypes = this.refactoringTypes.size();
        return add;
    }

    public boolean addMultipleRefactoringNames(Set<String> refactoringNames) {
        return this.refactoringNames.addAll(refactoringNames);
    }
}
