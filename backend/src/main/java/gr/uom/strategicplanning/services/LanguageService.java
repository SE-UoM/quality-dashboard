package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.*;
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

    @Autowired
    private ProjectLanguageRepository projectLanguageRepository;
    @Autowired
    private OrganizationLanguageRepository organizationLanguageRepository;

    @Autowired
    public LanguageService(@Value("${github.token}") String githubToken) {
        this.sonarApiClient = new SonarApiClient();
    }

    public void updateOrganizationLanguages(Organization organization) {
        Collection<Project> projects = organization.getProjects();
        OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
        Collection<OrganizationLanguage> organizationLanguages = organizationAnalysis.getLanguages();

        // make all languages have a total loc of 0
        resetOrgLanguagesLoc(organization);

        // Now recalculate the total loc for each language
        for (Project project : projects) {
            Collection<ProjectLanguage> projectLanguages = project.getLanguages();

            for (ProjectLanguage projectLanguage : projectLanguages) {
                Optional<OrganizationLanguage> organizationLanguage = organizationLanguageRepository.findByName(projectLanguage.getName());

                createOrUpdateOrgLanguage(projectLanguage, organizationLanguage, organization);
            }
        }
    }

    private OrganizationLanguage createOrUpdateOrgLanguage(ProjectLanguage projectLanguage, Optional<OrganizationLanguage> organizationLanguageOptional, Organization organization) {
        OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();

        if (organizationLanguageOptional.isEmpty()) {
            OrganizationLanguage newOrganizationLanguage = new OrganizationLanguage();
            newOrganizationLanguage.setName(projectLanguage.getName());
            newOrganizationLanguage.setLinesOfCode(projectLanguage.getLinesOfCode());
            newOrganizationLanguage.setOrganizationAnalysis(organizationAnalysis);

            organizationAnalysis.addLanguage(newOrganizationLanguage);

            return organizationLanguageRepository.save(newOrganizationLanguage);
        }

        OrganizationLanguage organizationLanguageToUpdate = organizationLanguageOptional.get();
        int currentLoc = organizationLanguageToUpdate.getLinesOfCode();
        int projectLanguageLoc = projectLanguage.getLinesOfCode();
        int totalLoc = currentLoc + projectLanguageLoc;

        organizationLanguageToUpdate.setLinesOfCode(totalLoc);

        return organizationLanguageRepository.save(organizationLanguageToUpdate);
    }

    private void resetOrgLanguagesLoc(Organization organization) {
        Collection<OrganizationLanguage> organizationLanguages = organization.getOrganizationAnalysis().getLanguages();

        for (OrganizationLanguage organizationLanguage : organizationLanguages) {
            organizationLanguage.setLinesOfCode(0);
            organizationLanguageRepository.save(organizationLanguage);
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
