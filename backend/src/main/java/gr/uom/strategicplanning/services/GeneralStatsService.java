package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.*;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.repositories.GeneralStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.jca.endpoint.GenericMessageEndpointFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class GeneralStatsService {

    @Autowired
    private GeneralStatsRepository generalStatsRepository;

    @Autowired
    private LanguageService languageService;

    public GeneralStats getGeneralStats(Organization organization) {
        GeneralStats generalStats = organization.getOrganizationAnalysis().getGeneralStats();

        Collection<OrganizationLanguage> languages = languageService.getOrganizationLanguages();

        generalStats.setTotalDevs(organization.getProjects()
                .stream().mapToInt(Project::getTotalDevelopers).sum());

        generalStats.setTotalFiles(countTotalFiles(organization));

        generalStats.setTotalCommits(organization.getProjects()
                .stream().mapToInt(project -> project.getCommits().size()).sum());

        generalStats.setTotalLinesOfCode(organization.getProjects().stream()
                .mapToInt(project -> project.getCommits().stream()
                        .mapToInt(Commit::getTotalLoC).sum()).sum());
        generalStats.setTotalLanguages(organization.getProjects().stream()
                .mapToInt(project -> project.getLanguages().size()).sum());
        generalStats.setTotalProjects(organization.getProjects().size());

        //TODO: Implement this
//        generalStats.setTopLanguages();

        generalStats.setOrganizationAnalysis(organization.getOrganizationAnalysis());

        saveGeneralStats(generalStats);

        return generalStats;
    }

    private int countTotalFiles(Organization organization) {
        int totalFiles = 0;
        for(Project project: organization.getProjects()) {
            totalFiles += project.getProjectStats().getTotalFiles();
        }
        return totalFiles;
    }

    private void saveGeneralStats(GeneralStats generalStats) {
        generalStatsRepository.save(generalStats);
    }
}
