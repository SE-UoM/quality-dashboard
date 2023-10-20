package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LanguageDistributionResponse implements ResponseInterface {
    private int totalLanguages;
    private Collection<LanguageResponse> languageDistribution;
}
