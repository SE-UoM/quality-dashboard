package gr.uom.strategicplanning.controllers.dtos;

import gr.uom.strategicplanning.controllers.responses.ProjectResponse;
import gr.uom.strategicplanning.models.domain.CodeSmell;
import gr.uom.strategicplanning.models.domain.OrganizationCodeSmellDistribution;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import lombok.*;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TechDebtStatsDTO {
    private Long id;
    private float totalTechDebt;
    private float averageTechDebt;
    private float averageTechDebtPerLoC;
    private int totalCodeSmells;
    private Long organizationAnalysisId;
    private ProjectResponse projectWithMinTechDebt;
    private ProjectResponse projectWithMaxTechDebt;
    private Collection<ProjectResponse> bestTechDebtProjects;
    private Collection<ProjectResponse> bestCodeSmellProjects;
    private Collection<OrganizationCodeSmellDistribution> codeSmells;

    public TechDebtStatsDTO(TechDebtStats techDebtStats) {
        this.id = techDebtStats.getId();
        this.totalTechDebt = techDebtStats.getTotalTechDebt();
        this.averageTechDebt = techDebtStats.getAverageTechDebt();
        this.averageTechDebtPerLoC = techDebtStats.getAverageTechDebtPerLoC();
        this.totalCodeSmells = techDebtStats.getTotalCodeSmells();
        this.organizationAnalysisId = techDebtStats.getOrganizationAnalysis().getId();
        this.projectWithMinTechDebt = new ProjectResponse(techDebtStats.getProjectWithMinTechDebt());
        this.projectWithMaxTechDebt = new ProjectResponse(techDebtStats.getProjectWithMaxTechDebt());
        this.bestTechDebtProjects = ProjectResponse.convertToProjectResponseList( (List) techDebtStats.getBestTechDebtProjects());
        this.bestCodeSmellProjects = ProjectResponse.convertToProjectResponseList((List) techDebtStats.getBestCodeSmellProjects());

        this.codeSmells = techDebtStats.getCodeSmells();
    }
}
