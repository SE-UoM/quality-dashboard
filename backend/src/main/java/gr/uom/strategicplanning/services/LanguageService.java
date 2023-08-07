package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    private LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }
    public Language getLanguageByName(String languageName) {
        return languageRepository.findByName(languageName);
    }

    public void saveLanguage(Language newLanguage) {
        languageRepository.save(newLanguage);
    }
}
