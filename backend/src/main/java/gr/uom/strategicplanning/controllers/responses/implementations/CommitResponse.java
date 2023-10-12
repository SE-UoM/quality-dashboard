package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.models.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommitResponse {

    private Long id;
    private String hash;
    private DeveloperResponse developer;
    private Date commitDate;
    private Collection<CodeSmell> codeSmells;
    private double technicalDebt;
    private int totalFiles;
    private int totalLoC;
    private int totalCodeSmells;
    private double techDebtPerLoC;
    private Long projectId;

    public CommitResponse(Commit commit) {
        this.id = commit.getId();
        this.hash = commit.getHash();

        Developer developer = commit.getDeveloper();
        this.developer = new DeveloperResponse(developer);
        this.commitDate = commit.getCommitDate();
        this.codeSmells = commit.getCodeSmells();
        this.technicalDebt = commit.getTechnicalDebt();
        this.totalFiles = commit.getTotalFiles();
        this.totalLoC = commit.getTotalLoC();
        this.totalCodeSmells = commit.getTotalCodeSmells();
        this.techDebtPerLoC = commit.getTechDebtPerLoC();
        this.projectId = commit.getProject().getId();
    }

    public static Collection<CommitResponse> convertToCommitResponseCollection(Collection<Commit> commitCollection) {
        Collection<CommitResponse> commitResponses = new ArrayList<>();

        for (Commit commit : commitCollection) {
            commitResponses.add(new CommitResponse(commit));
        }

        return commitResponses;
    }
}
