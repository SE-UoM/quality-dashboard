package uom.qualitydashboard.projectsubmissionservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class SubmittedProject {
    @Id
    @SequenceGenerator(
            name = "submitted_project_sequence",
            sequenceName = "submitted_project_sequence"
    )
    @GeneratedValue(generator = "submitted_project_sequence")
    private Long id;

    private String repoUrl;
    private Date submissionDate;
    private Date lastAnalysisDate;
    private Long userId;
    private Long organizationId;
}
