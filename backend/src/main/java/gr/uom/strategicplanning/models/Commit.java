package gr.uom.strategicplanning.models;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Number technicalDebt;
    private Number totalFiles;
    private Number totalLoC;
    private Number totalCodeSmells;
    private Number techDebtPerLoC;
    @OneToMany
    private Collection<Language> languages;
    private int totalLanguages;
    @ManyToOne
    private Project project;

    public void setCodeSmells(Collection<CodeSmell> codeSmells) {
        this.codeSmells = codeSmells;
        this.totalCodeSmells = codeSmells.size();
    }

    public void setLanguages(Collection<Language> languages) {
        this.languages = languages;
        this.totalLanguages = languages.size();
    }

    public void calculateTechDebtPerLoC() {
        this.techDebtPerLoC = this.technicalDebt.doubleValue() / this.totalLoC.doubleValue();
    }

    @Override
    // Pretty print the Commit object
    public String toString() {
        return "Commit{\n" +
                "id=" + id + "\n" +
                ", hash='" + hash + '\'' + "\n" +
                ", developer=" + developer + "\n" +
                ", commitDate=" + commitDate + "\n" +
                ", codeSmells=" + codeSmells + "\n" +
                ", technicalDebt=" + technicalDebt + "\n" +
                ", totalFiles=" + totalFiles + "\n" +
                ", totalLoC=" + totalLoC + "\n" +
                ", totalCodeSmells=" + totalCodeSmells + "\n" +
                ", techDebtPerLoC=" + techDebtPerLoC + "\n" +
                ", languages=" + languages + "\n" +
                ", totalLanguages=" + totalLanguages + "\n" +
                '}';
    }
}
