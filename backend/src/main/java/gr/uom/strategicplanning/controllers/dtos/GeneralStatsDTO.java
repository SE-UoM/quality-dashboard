package gr.uom.strategicplanning.controllers.dtos;

import gr.uom.strategicplanning.controllers.responses.LanguageResponse;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import lombok.*;

import java.util.List;
import java.util.Map;

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

    private List<LanguageResponse> languages;
    private Map<Integer, LanguageResponse> topLanguages;

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

    private void convertTopLanguages(Map<Integer, LanguageStats> topLanguages) {
        for (Map.Entry<Integer, LanguageStats> entry : topLanguages.entrySet()) {
            LanguageResponse languageResponse = new LanguageResponse(entry.getValue());
            this.topLanguages.put(entry.getKey(), languageResponse);
        }
    }

    private void convertLanguages(List<LanguageStats> languageStats) {
        for (LanguageStats languageStat : languageStats) {
            LanguageResponse languageResponse = new LanguageResponse(languageStat);
            this.languages.add(languageResponse);
        }
    }
}
