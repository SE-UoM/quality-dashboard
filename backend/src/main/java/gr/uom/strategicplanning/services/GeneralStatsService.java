package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.repositories.GeneralStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralStatsService {

    @Autowired
    private GeneralStatsRepository generalStatsRepository;

    @Autowired
    private LanguageService languageService;

    public GeneralStats getGeneralStats(Organization organization) {
        GeneralStats generalStats = new GeneralStats();
        Optional<GeneralStats> generalStatsOptional = generalStatsRepository.findByOrganization(organization);

        if (generalStatsOptional.isPresent()) {
            generalStats = generalStatsOptional.get();
        }

        generalStats.setLanguages(languageService.getLanguages(organization));
        generalStats.setTotalDevs(organization.getProjects()
                .stream().mapToInt(Project::getTotalDevelopers).sum());
        generalStats.setTotalFiles(organization.getProjects()
                .stream().mapToInt(project -> project.getCommits().stream().mapToInt(Commit::getTotalFiles).sum()).sum());
        generalStats.setTotalCommits(organization.getProjects()
                .stream().mapToInt(project -> project.getCommits().size()).sum());
        generalStats.setTotalLinesOfCode(organization.getProjects().stream()
                .mapToInt(project -> project.getCommits().stream()
                        .mapToInt(Commit::getTotalLoC).sum()).sum());
        generalStats.setTotalLanguages(organization.getProjects().stream()
                .mapToInt(project -> project.getLanguages().size()).sum());

        //TODO: Implement this
//        generalStats.setTopLanguages();


        return generalStats;
    }
}
