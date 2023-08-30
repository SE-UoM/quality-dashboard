package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.repositories.LanguageRepository;
import gr.uom.strategicplanning.repositories.LanguageStatsRepository;
import gr.uom.strategicplanning.repositories.OrganizationLanguageRepository;
import gr.uom.strategicplanning.repositories.ProjectLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class LanguageService {

    private final SonarApiClient sonarApiClient;
    private LanguageRepository languageRepository;
    private LanguageStatsRepository languageStatsRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectLanguageRepository projectLanguageRepository;
    @Autowired
    private OrganizationLanguageRepository organizationLanguageRepository;

    @Autowired
    public LanguageService(LanguageRepository languageRepository, @Value("${github.token}") String githubToken, LanguageStatsRepository languageStatsRepository) {
        this.languageRepository = languageRepository;
        this.sonarApiClient = new SonarApiClient();
        this.languageStatsRepository = languageStatsRepository;
    }
    public Optional<Language> getLanguageByName(String languageName) {
        return languageRepository.findByName(languageName);
    }

    public void saveLanguage(Language newLanguage) {
        languageRepository.save(newLanguage);
    }

    public void updateOrganizationLanguages(Project project) {
        Organization organization = project.getOrganization();
        Collection<ProjectLanguage> projectLanguages = project.getLanguages();
        GeneralStats generalStats = organization.getOrganizationAnalysis().getGeneralStats();

        for (ProjectLanguage lang : projectLanguages) {
            String currentLangName = lang.getName();
            Optional<OrganizationLanguage> existingLanguage = organizationLanguageRepository.findByName(currentLangName);

            if (existingLanguage.isEmpty()) {
                int currentLoC = lang.getLinesOfCode();
                String currentName = lang.getName();

                OrganizationLanguage newLanguage = new OrganizationLanguage();
                newLanguage.setName(currentName);
                newLanguage.setLinesOfCode(currentLoC);
                newLanguage.setGeneralStats(generalStats);

                organizationLanguageRepository.save(newLanguage);
            }
            else {
                OrganizationLanguage existingLang = existingLanguage.get();
                int existingLoC = existingLang.getLinesOfCode();
                int currentLoC = lang.getLinesOfCode();
                int newLoC = currentLoC + existingLoC;

                existingLang.setLinesOfCode(newLoC);
                organizationLanguageRepository.save(existingLang);
            }
        }

    }

    public Collection<ProjectLanguage> extractLanguagesFromProject(Project project) throws IOException {
        Collection<ProjectLanguage> languages = sonarApiClient.fetchLanguages(project);

        for (ProjectLanguage language : languages) {
            Optional<ProjectLanguage> projectLanguage = projectLanguageRepository.findByProjectIdAndLanguage(project.getId(), language.getName());

            if (projectLanguage.isEmpty())
                projectLanguageRepository.save(language);
            else {
                ProjectLanguage projectLanguageToUpdate = projectLanguage.get();
                projectLanguageToUpdate.setLinesOfCode(language.getLinesOfCode());
                projectLanguageRepository.save(projectLanguageToUpdate);
            }
        }

        return languages;
    }

    public Collection<OrganizationLanguage> getOrganizationLanguages() {
        return organizationLanguageRepository.findAll();
    }
}
