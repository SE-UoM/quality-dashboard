package gr.uom.strategicplanning.models.stats;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityStats {
    
    @Id
    @GeneratedValue
    private Long id;
    private float commitsPerDay = 0;
    private float locAddedPerDay = 0;
    private float filesAddedPerDay = 0;
    private float projectsAddedPerDay = 0;
    private float averageLoC;
    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public ActivityStats(OrganizationAnalysis organizationAnalysis) {
        this.organizationAnalysis = organizationAnalysis;
    }
}
