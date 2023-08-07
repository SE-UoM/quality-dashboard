package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class LanguageService {

    private LanguageRepository languageRepository;
    public GithubApiClient githubApiClient = new GithubApiClient();

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }
    public Language getLanguageByName(String languageName) {
        return languageRepository.findByName(languageName);
    }

    public void saveLanguage(Language newLanguage) {
        languageRepository.save(newLanguage);
    }

    public Collection<Language> extractLanguages(Project project) {
        List<Language> listLanguages = new ArrayList<>();
        Map<String, Integer> map = githubApiClient.languageRespone(project);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String languageName = entry.getKey();

            Language existingLanguage = getLanguageByName(languageName);

            if (existingLanguage == null) {
                Language newLanguage = new Language();
                newLanguage.setName(languageName);
                saveLanguage(newLanguage);
                listLanguages.add(newLanguage);
            } else {
                listLanguages.add(existingLanguage);
            }
        }

        return listLanguages;

    }
}
