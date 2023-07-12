package gr.uom.strategicplanning.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitAnalysis {

    @Id
    private Long id;
    @OneToMany
    private Collection<CodeSmell> codeSmells;
    private float technicalDebt;
    private int totalFiles;
    private int totalLoC;
    private int totalCodeSmells;
    private float techDebtPerLoC;
    @OneToMany
    private Collection<Language> languages;
    private int totalLanguages;
}
