package gr.uom.strategicplanning.controllers.dtos;

import gr.uom.strategicplanning.models.stats.GeneralStats;
import lombok.*;

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

    public GeneralStatsDTO(GeneralStats generalStats) {
        this.id = generalStats.getId();
        this.totalProjects = generalStats.getTotalProjects();
        this.totalLanguages = generalStats.getTotalLanguages();
        this.totalCommits = generalStats.getTotalCommits();
        this.totalFiles = generalStats.getTotalFiles();
        this.totalLinesOfCode = generalStats.getTotalLinesOfCode();
        this.totalDevs = generalStats.getTotalDevs();
    }
}
