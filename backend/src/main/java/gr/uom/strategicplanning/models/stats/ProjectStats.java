package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.domain.Project;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectStats {
    @Id
    @GeneratedValue
    private Long id;
    private Number totalLoC;
    private Number totalFiles;
    private Number totalCodeSmells;
    private Number techDebt;
    private Number techDebtPerLoC;
    private Number totalLanguages;

    @OneToOne
    @ToString.Exclude
    private Project project;

    public void calculateTechDebtPerLoC() {
        this.techDebtPerLoC = this.techDebt.doubleValue() / this.totalLoC.doubleValue();
    }
}
