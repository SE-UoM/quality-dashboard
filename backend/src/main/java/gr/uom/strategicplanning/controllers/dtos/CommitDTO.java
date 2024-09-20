package gr.uom.strategicplanning.controllers.dtos;

import gr.uom.strategicplanning.models.domain.Commit;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommitDTO {
    private Long id;
    private String hash;
    private String date;
    private double technicalDebt;
    private int totalFiles;
    private int totalLoC;
    private int totalCodeSmells;
    private double techDebtPerLoC;
    private double dmmUnitSize;
    private double dmmComplexity;
    private double dmmInterfacing;
    private String maintainabilityRating;
    private String developer;
    private String project;

    public CommitDTO(Commit commit) {
        this.id = commit.getId();
        this.hash = commit.getHash();
        this.date = commit.getCommitDate().toString();
        this.technicalDebt = commit.getTechnicalDebt();
        this.totalFiles = commit.getTotalFiles();
        this.totalLoC = commit.getTotalLoC();
        this.totalCodeSmells = commit.getTotalCodeSmells();
        this.techDebtPerLoC = commit.getTechDebtPerLoC();
        this.dmmUnitSize = commit.getDmmUnitSize();
        this.dmmComplexity = commit.getDmmComplexity();
        this.dmmInterfacing = commit.getDmmInterfacing();
        this.maintainabilityRating = commit.getMaintainabilityRating();
        this.developer = commit.getDeveloper().getGithubUrl();
        this.project = commit.getProject().getName();
    }

    public static CommitDTO from(Commit commit) {
        return new CommitDTO(commit);
    }
}
