package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.controllers.dtos.ActivityStatsDTO;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.repositories.ActivityStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityStatsService {

    private ActivityStatsRepository activityStatsRepository;

    @Autowired
    public ActivityStatsService(ActivityStatsRepository activityStatsRepository) {
        this.activityStatsRepository = activityStatsRepository;
    }

    public ActivityStats getActivityStats(Organization organization) {
        ActivityStats activityStats = organization.getOrganizationAnalysis().getActivityStats();

        List<Project> projects = organization.getProjects();

        int locPerCommitPerProject = calculateValues(projects, "loc");
        int allCommits = calculateValues(projects, "commits");
        int allProjects = projects.size();
        int addedFiles = calculateValues(projects, "files");

        float avgLOC = calcAverageLoC(locPerCommitPerProject, allCommits);
        activityStats.setAverageLoC(avgLOC);

        activityStats.setFilesAddedPerAnalysis(addedFiles - activityStats.getFilesAddedPerAnalysis());

        activityStats.setCommitsPerAnalysis(allCommits - activityStats.getCommitsPerAnalysis());

        activityStats.setLocAddedPerAnalysis(locPerCommitPerProject - activityStats.getLocAddedPerAnalysis());

        activityStats.setProjectsAddedPerAnalysis(allProjects - activityStats.getProjectsAddedPerAnalysis());

        activityStats.setOrganizationAnalysis(organization.getOrganizationAnalysis());

        saveActivityStats(activityStats);

        return activityStats;
    }

    private void saveActivityStats(ActivityStats activityStats) {
        activityStatsRepository.save(activityStats);
    }

    private float calcAverageLoC(int locPerCommitPerProject, int allCommits) {
        if (allCommits == 0) {
            return 0;
        }
        return (float) locPerCommitPerProject / allCommits;
    }

    private int calculateValues(List<Project> projects, String param) {
        int count = 0;
        for (Project project : projects) {
            for (Commit commit : project.getCommits()) {
                if (param.equals("loc")) {
                    if (commit.getTotalLoC() >= 0) {
                        count += commit.getTotalLoC();
                    }
                } else if (param.equals("files")) {
                    if (commit.getTotalFiles() >= 0) {
                        count += commit.getTotalFiles();
                    }
                }
            }
            if (param.equals("projects")) {
                count += project.getTotalCommits();
            }
        }
        return count;
    }
}
