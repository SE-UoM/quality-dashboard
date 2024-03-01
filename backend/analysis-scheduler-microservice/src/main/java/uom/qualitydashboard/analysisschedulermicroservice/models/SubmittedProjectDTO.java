package uom.qualitydashboard.analysisschedulermicroservice.models;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class SubmittedProjectDTO {
    private Long id;
    private String repoUrl;
    private Date submissionDate;
    private Date lastAnalysisDate;
    private Long userId;
    private Long organizationId;
}
