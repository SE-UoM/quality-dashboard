package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.models.domain.OrganizationLanguage;
import gr.uom.strategicplanning.models.domain.ProjectLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LanguageResponse {
    public Long id;
    public String name;
    public String imageUrl;
    public int linesOfCode;

    public LanguageResponse(ProjectLanguage projectLanguage) {
        this.id = projectLanguage.getId();
        this.name = projectLanguage.getName();
        this.linesOfCode = projectLanguage.getLinesOfCode();
    }

    public LanguageResponse(OrganizationLanguage projectLanguage) {
        this.id = projectLanguage.getId();
        this.name = projectLanguage.getName();
        this.linesOfCode = projectLanguage.getLinesOfCode();
    }

    public static Collection<LanguageResponse> convertToLanguageResponseCollection(Collection<ProjectLanguage> languageCollection) {
        Collection<LanguageResponse> languageResponses = new ArrayList<>();

        for (ProjectLanguage languageResponse : languageCollection) {
            languageResponses.add(new LanguageResponse(languageResponse));
        }

        return languageResponses;
    }
}

