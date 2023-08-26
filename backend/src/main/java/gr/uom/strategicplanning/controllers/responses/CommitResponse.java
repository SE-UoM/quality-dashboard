package gr.uom.strategicplanning.controllers.responses;

import gr.uom.strategicplanning.models.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommitResponse {

    private Long id;
    private String hash;
    private Developer developer;
    private Date commitDate;
    private Collection<CodeSmell> codeSmells;
    private double technicalDebt;
    private int totalFiles;
    private int totalLoC;
    private int totalCodeSmells;
    private double techDebtPerLoC;
    private Collection<LanguageStats> languages;
    private int totalLanguages;
    private Long projectId;

    public CommitResponse(Commit commit) {
        this.id = commit.getId();
        this.hash = commit.getHash();
        this.developer = commit.getDeveloper();
        this.commitDate = commit.getCommitDate();
        this.codeSmells = commit.getCodeSmells();
        this.technicalDebt = commit.getTechnicalDebt();
        this.totalFiles = commit.getTotalFiles();
        this.totalLoC = commit.getTotalLoC();
        this.totalCodeSmells = commit.getTotalCodeSmells();
        this.techDebtPerLoC = commit.getTechDebtPerLoC();
//        this.languages = commit.getLanguages();
//        this.totalLanguages = commit.getTotalLanguages();
        this.projectId = commit.getProject().getId();
    }
}
