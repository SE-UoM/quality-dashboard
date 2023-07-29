package gr.uom.strategicplanning.models.analyses;

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
    @OneToOne
    private GeneralStats generalStats;
    @OneToOne
    private TechDebtStats techDebtStats;
    @OneToOne
    private ActivityStats activityStats;
    @OneToOne
    private Project mostStarredProject;
    @OneToOne
    private Project mostForkedProject;
}