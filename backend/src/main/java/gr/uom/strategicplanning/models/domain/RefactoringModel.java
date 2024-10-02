package gr.uom.strategicplanning.models.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.refactoringminer.api.Refactoring;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefactoringModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refactoringType;
    @Column(length = 1000)
    private String name;
    @Column(length = 5000)
    private String description;
    private int involvedClassesBefore;
    private int involvedClassesAfter;
    @ManyToOne
    private Commit commit;

    public RefactoringModel(Refactoring ref) {
        this.refactoringType = ref.getRefactoringType().getDisplayName();
        this.name = ref.getName();
        this.description = ref.toString();
        this.involvedClassesBefore = ref.getInvolvedClassesBeforeRefactoring().size();
        this.involvedClassesAfter = ref.getInvolvedClassesAfterRefactoring().size();
    }

}
