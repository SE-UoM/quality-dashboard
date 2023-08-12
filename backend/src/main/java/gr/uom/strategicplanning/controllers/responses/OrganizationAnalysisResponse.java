package gr.uom.strategicplanning.controllers.responses;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrganizationAnalysisResponse {

    private Long id;
    private String orgName;
    private Date analysisDate;
    private GeneralStats generalStats;
    private TechDebtStats techDebtStats;
    private ActivityStats activityStats;
    private ProjectResponse mostStarredProject;
    private ProjectResponse mostForkedProject;
    private Collection<LanguageResponse> languages = null;

    public OrganizationAnalysisResponse(OrganizationAnalysis organizationAnalysis) {
        this.id = organizationAnalysis.getId();
        this.orgName = organizationAnalysis.getOrgName();
        this.analysisDate = organizationAnalysis.getAnalysisDate();
        this.generalStats = organizationAnalysis.getGeneralStats();
        this.techDebtStats = organizationAnalysis.getTechDebtStats();
        this.activityStats = organizationAnalysis.getActivityStats();
        this.mostStarredProject = new ProjectResponse(organizationAnalysis.getMostStarredProject());
        this.mostForkedProject = new ProjectResponse(organizationAnalysis.getMostForkedProject());
    }
}
