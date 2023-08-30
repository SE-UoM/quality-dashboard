package gr.uom.strategicplanning.controllers.dtos;

import gr.uom.strategicplanning.controllers.responses.LanguageResponse;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.models.domain.OrganizationLanguage;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class GeneralStatsDTO {
    private Long id;
    private int totalProjects;
    private int totalLanguages;
    private int totalCommits;
    private int totalFiles;
    private int totalLinesOfCode;
    private int totalDevs;

    private List<LanguageResponse> languages = new ArrayList<>();
    private Map<Integer, LanguageResponse> topLanguages = new HashMap<>();

    private Long organizationAnalysisId;

    public GeneralStatsDTO(GeneralStats generalStats) {
        this.id = generalStats.getId();
        this.totalProjects = generalStats.getTotalProjects();
        this.totalLanguages = generalStats.getTotalLanguages();
        this.totalCommits = generalStats.getTotalCommits();
        this.totalFiles = generalStats.getTotalFiles();
        this.totalLinesOfCode = generalStats.getTotalLinesOfCode();
        this.totalDevs = generalStats.getTotalDevs();
        this.organizationAnalysisId = generalStats.getOrganizationAnalysis().getId();

        convertLanguages(generalStats.getLanguages());
        convertTopLanguages(generalStats.getTopLanguages());
    }

    private void convertTopLanguages(Map<Integer, OrganizationLanguage> topLanguages) {
        for (Map.Entry<Integer, OrganizationLanguage> entry : topLanguages.entrySet()) {
            LanguageResponse languageResponse = new LanguageResponse(entry.getValue());
            this.topLanguages.put(entry.getKey(), languageResponse);
        }
    }

    private void convertLanguages(Collection<OrganizationLanguage> languageStats) {
        for (OrganizationLanguage languageStat : languageStats) {
            LanguageResponse languageResponse = new LanguageResponse(languageStat);
            this.languages.add(languageResponse);
        }
    }
}
