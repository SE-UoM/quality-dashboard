package gr.uom.strategicplanning.models.analyses;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationAnalysis {
    public static final int COMMITS_THRESHOLD = 50;

    @Id
    @GeneratedValue
    private Long id;
    private String orgName;
    private Date analysisDate;
    @OneToOne(cascade = CascadeType.ALL)
    private GeneralStats generalStats = new GeneralStats();
    @OneToOne(cascade = CascadeType.ALL)
    private TechDebtStats techDebtStats = new TechDebtStats(this);
    @OneToOne(cascade = CascadeType.ALL)
    private ActivityStats activityStats = new ActivityStats();
    @OneToOne(cascade = CascadeType.ALL)
    private Project mostStarredProject;
    @OneToOne(cascade = CascadeType.ALL)
    private Project mostForkedProject;
    @OneToOne
    private Organization organization;
}