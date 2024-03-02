package uom.qualitydashboard.analysisschedulermicroservice.models;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class GithubAnalysisDTO {
    private Long id;
    private Long projectId;
    private Long organizationId;
    private String projectName;
    private String projectFullName;
    private String projectDescription;
    private String projectUrl;
    private String defaultBranch;
    private int stars;
    private int forks;
    private int openIssues;
    private int totalCommits;
    private Date lastAnalysisDate = new Date();
}
