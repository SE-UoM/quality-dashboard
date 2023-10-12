package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.controllers.dtos.ActivityStatsDTO;
import gr.uom.strategicplanning.controllers.dtos.GeneralStatsDTO;
import gr.uom.strategicplanning.controllers.dtos.TechDebtStatsDTO;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.OrganizationLanguage;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrganizationAnalysisResponse implements ResponseInterface {

    private Long id;
    private String orgName;
    private Date analysisDate;
    private GeneralStatsDTO generalStats;
    private TechDebtStatsDTO techDebtStats;
    private ActivityStatsDTO activityStats;
    private ProjectResponse mostStarredProject;
    private ProjectResponse mostForkedProject;
    private Collection<LanguageResponse> languages;
    private Map<Integer, LanguageResponse> topLanguages;

    public OrganizationAnalysisResponse(OrganizationAnalysis organizationAnalysis) {
        this.id = organizationAnalysis.getId();
        this.orgName = organizationAnalysis.getOrgName();
        this.analysisDate = organizationAnalysis.getAnalysisDate();

        GeneralStats generalStats = organizationAnalysis.getGeneralStats();
        this.generalStats = new GeneralStatsDTO(generalStats);

        TechDebtStats techDebtStats = organizationAnalysis.getTechDebtStats();
        this.techDebtStats = new TechDebtStatsDTO(techDebtStats);

        ActivityStats activityStats = organizationAnalysis.getActivityStats();
        this.activityStats = new ActivityStatsDTO(activityStats);

        this.mostStarredProject = new ProjectResponse(organizationAnalysis.getMostStarredProject());
        this.mostForkedProject = new ProjectResponse(organizationAnalysis.getMostForkedProject());

        Collection<LanguageResponse> languages = convertLanguages(organizationAnalysis.getLanguages());
        this.languages = languages;

        Map<Integer, LanguageResponse> topLanguages = convertTopLanguages(organizationAnalysis.getTopLanguages());
        this.topLanguages = topLanguages;
    }

    private Map<Integer, LanguageResponse> convertTopLanguages(Map<Integer, OrganizationLanguage> topLanguages) {
        Map<Integer, LanguageResponse> convertedTopLanguages = new HashMap<>();

        for (Map.Entry<Integer, OrganizationLanguage> entry : topLanguages.entrySet()) {
            LanguageResponse languageResponse = new LanguageResponse(entry.getValue());
            convertedTopLanguages.put(entry.getKey(), languageResponse);
        }

        return convertedTopLanguages;
    }

    private Collection<LanguageResponse> convertLanguages(Collection<OrganizationLanguage> languageStats) {
        ArrayList<LanguageResponse> convertedLanguages = new ArrayList<>();

        for (OrganizationLanguage languageStat : languageStats) {
            LanguageResponse languageResponse = new LanguageResponse(languageStat);
            convertedLanguages.add(languageResponse);
        }

        return convertedLanguages;
    }
}
