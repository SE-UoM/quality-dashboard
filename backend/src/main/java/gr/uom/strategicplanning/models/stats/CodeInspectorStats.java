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
    private Integer totalOutliers;
    private Integer totalHotspots;

    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

    public CodeInspectorStats(OrganizationAnalysis organizationAnalysis){
        this.organizationAnalysis = organizationAnalysis;
    }

    public void addGitUrl(String gitUrl){
        boolean exists = gitUrls.contains(gitUrl);
        if (!exists) gitUrls.add(gitUrl);
    }
}
