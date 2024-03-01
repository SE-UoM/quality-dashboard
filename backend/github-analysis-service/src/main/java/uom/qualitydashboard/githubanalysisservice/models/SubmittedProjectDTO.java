package uom.qualitydashboard.githubanalysisservice.models;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class SubmittedProjectDTO {
    private Long id;
    private String repoUrl;
    private Date submissionDate;
    private Date lastAnalysisDate;
    private Long userId;
    private Long organizationId;
}
