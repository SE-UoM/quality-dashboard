package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeInspectorStats {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private Set<String> gitUrls;
    private Date firstDateAnalysis;
    private Date lastDateAnalysis;
    private Long averageComplexity;
    private Long averageChurn;
    private Long averageNloc;
    private Integer totalNloc;
    private Integer totalFiles;
    private Integer totalOutliers;
    private Integer totalPrioritizedFiles;

    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public CodeInspectorStats(OrganizationAnalysis organizationAnalysis){
        this.organizationAnalysis = organizationAnalysis;
    }

}
