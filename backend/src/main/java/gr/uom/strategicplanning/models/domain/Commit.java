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
    @OneToOne
    private Developer developer;
    private Date commitDate;
    @OneToMany
    private Collection<CodeSmell> codeSmells;
    private double technicalDebt;
    private int totalFiles;
    private int totalLoC;
    private int totalCodeSmells;
    private double techDebtPerLoC;
/*    @OneToMany
    private Collection<LanguageStats> languages;*/
    private int totalLanguages;
    @ManyToOne
    private Project project;

    public void setCodeSmells(Collection<CodeSmell> codeSmells) {
        this.codeSmells = codeSmells;
        this.totalCodeSmells = codeSmells.size();
    }

//    public void setLanguages(Collection<LanguageStats> languages) {
//        this.languages = languages;
//        this.totalLanguages = languages.size();
//    }

    public void calculateTechDebtPerLoC() {
        this.techDebtPerLoC = this.technicalDebt / this.totalLoC;
    }
}
