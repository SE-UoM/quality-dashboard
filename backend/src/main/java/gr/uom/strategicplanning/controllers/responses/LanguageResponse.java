package gr.uom.strategicplanning.controllers.responses;

import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LanguageResponse {
    public Long id;
    public String name;
    public String imageUrl;
    public int linesOfCode;

    public LanguageResponse(LanguageStats languageStats) {
        this.id = languageStats.getLanguage().getId();
        this.name = languageStats.getLanguage().getName();
        this.imageUrl = languageStats.getLanguage().getImageUrl();
        this.linesOfCode = languageStats.getLinesOfCode();
    }
}

