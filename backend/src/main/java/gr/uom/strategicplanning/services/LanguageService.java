package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class LanguageService {

    @Value("${github.token}")
    private String githubToken;
    private LanguageRepository languageRepository;
    public GithubApiClient githubApiClient = new GithubApiClient(githubToken);

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }
    public Optional<Language> getLanguageByName(String languageName) {
        return languageRepository.findByName(languageName);
    }

    public void saveLanguage(Language newLanguage) {
        languageRepository.save(newLanguage);
    }

    public Collection<LanguageStats> extractLanguages(Project project) throws IOException {
        List<LanguageStats> listLanguages = new ArrayList<>();
        Map<String, Integer> map = githubApiClient.languageResponse(project);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String languageName = entry.getKey();
            LanguageStats languageStats = new LanguageStats();

            Optional<Language> foundLanguage = getLanguageByName(languageName);
            Language existingLanguage = foundLanguage.orElse(null);

            if (existingLanguage == null) {
                Language newLanguage = new Language();
                newLanguage.setName(languageName);
                saveLanguage(newLanguage);
                languageStats.setLanguage(newLanguage);
            } else {
                languageStats.setLanguage(existingLanguage);
            }
            languageStats.setLinesOfCode(entry.getValue());
            listLanguages.add(languageStats);
        }

        return listLanguages;

    }

    public List<LanguageStats> getLanguages(Organization organization) {
        List<LanguageStats> languages = new ArrayList<>();
        for (Project project : organization.getProjects()) {
            languages.addAll(project.getLanguages());
        }
        return languages;
    }
}
