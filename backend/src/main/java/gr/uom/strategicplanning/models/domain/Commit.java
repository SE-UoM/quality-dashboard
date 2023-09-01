package gr.uom.strategicplanning.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

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
    @OneToOne(cascade = CascadeType.PERSIST)
    private Developer developer;
    private Date commitDate;
    @OneToMany
    private Collection<CodeSmell> codeSmells;
    private double technicalDebt;
    private int totalFiles;
    private int totalLoC;
    private int totalCodeSmells;
    private double techDebtPerLoC;
    @ManyToOne
    private Project project;

    public void setCodeSmells(Collection<CodeSmell> codeSmells) {
        this.codeSmells = codeSmells;
        this.totalCodeSmells = codeSmells.size();
    }

    public void calculateTechDebtPerLoC() {
        this.techDebtPerLoC = this.technicalDebt / this.totalLoC;
    }
}
