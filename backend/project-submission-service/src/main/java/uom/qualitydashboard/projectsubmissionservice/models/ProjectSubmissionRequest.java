package uom.qualitydashboard.projectsubmissionservice.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ProjectSubmissionRequest {
    private String repoUrl;
    private Long userId;
}
