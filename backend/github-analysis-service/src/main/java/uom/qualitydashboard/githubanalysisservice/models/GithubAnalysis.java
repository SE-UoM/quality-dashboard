package uom.qualitydashboard.githubanalysisservice.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class GithubAnalysis {
    @Id
    @SequenceGenerator(
            name = "github_analysis_sequence",
            sequenceName = "github_analysis_sequence"
    )
    @GeneratedValue(generator = "github_analysis_sequence")
    private Long id;

    private Long projectId;
    private String projectName;
    private String projectFullName;
    private String projectDescription;
    private String projectUrl;
    private String defaultBranch;

    private int stars;
    private int forks;
    private int openIssues;
    private int totalCommits;

    @Builder.Default
    private Date lastAnalysisDate = new Date();
}
