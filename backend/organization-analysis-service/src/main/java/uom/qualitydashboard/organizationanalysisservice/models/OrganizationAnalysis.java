package uom.qualitydashboard.organizationanalysisservice.models;

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
@ToString
@Builder
public class OrganizationAnalysis {
    @Id
    @SequenceGenerator(name = "organization_analysis_sequence", sequenceName = "organization_analysis_sequence")
    @GeneratedValue(generator = "organization_analysis_sequence")
    private Long id;

    // General Organization Analysis Information
    private String organizationName;
    private Long organizationId;
    private Date date;

    // General Organization Statistics
    private int totalProjects;
    private int totalLanguages;
    private int totalCommits;
    private int totalFiles;
    private int totalLinesOfCode;
    private int totalDevs;

    // Technical Debt Statistics
    private float totalTechDebt = -1;
    private float averageTechDebt = -1;
    private Long projectWithMostTechDebt;
    private Long projectWithLeastTechDebt;

    // Activity Statistics
    private float commitsPerAnalysis = -1;
    private float locAddedPerAnalysis = -1;
    private float filesAddedPerAnalysis = -1;
    private float projectsAddedPerAnalysis = -1;
    private float averageLoC = -1;
}
