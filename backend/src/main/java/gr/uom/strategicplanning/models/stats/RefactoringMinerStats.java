package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefactoringMinerStats {

    @Id
    private Long id;

    private int totalRefactorings;
    private int totalRefactoringsInvolvingClasses;
    private int totalRefactoringsInvolvingMethods;
    @ElementCollection
    private List<String> refactoringTypes;
    @ElementCollection
    private List<String> refactoringNames;
    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public RefactoringMinerStats(OrganizationAnalysis organizationAnalysis) {
        this.organizationAnalysis = organizationAnalysis;

    }
}
