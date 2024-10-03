package gr.uom.strategicplanning.models.external;

import gr.uom.strategicplanning.models.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeInspectorProjectStats {

    @Id
    @GeneratedValue
    private Long id;
    private String projectName;
    private String projectUrl;
    private String fromDate;
    private String toDate;
    private Double averageChurn;
    private Integer totalOutliers;
    private Integer totalHotspots;
    private Integer highPriorityHotspots;
    private Integer mediumPriorityHotspots;
    private Integer normalPriorityHotspots;
    private Integer lowPriorityHotspots;
    private Integer unknownPriorityHotspots;
    @OneToOne
    private Project project;

    public CodeInspectorProjectStats(Project project){
        this.project = project;
    }
}
