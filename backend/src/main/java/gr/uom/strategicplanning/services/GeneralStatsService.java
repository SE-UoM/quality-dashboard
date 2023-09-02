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

    public int countTotalFiles(Organization organization) {
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
