package gr.uom.strategicplanning.models.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralStats {

    @Id
    @GeneratedValue
    private Long id;
    private int totalProjects;
    private int totalLanguages;
    @ManyToMany
    private List<LanguageStats> languages;
    @ManyToMany
    private Map <Integer, LanguageStats> topLanguages;
    private int totalCommits;
    private int totalFiles;
    private int totalLinesOfCode;
    private int totalDevs;
    @OneToOne
    @JsonIgnore
    private OrganizationAnalysis organizationAnalysis;

    public GeneralStats(OrganizationAnalysis organizationAnalysis) {
        this.organizationAnalysis = organizationAnalysis;
    }
}
