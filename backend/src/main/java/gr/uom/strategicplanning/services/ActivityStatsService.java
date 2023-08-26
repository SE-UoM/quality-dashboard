package gr.uom.strategicplanning.services;

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
        ActivityStats activityStats = new ActivityStats();
        Optional<ActivityStats> activityStatsOptional = activityStatsRepository.findByOrganizationAnalysis(organization.getOrganizationAnalysis());

        if (activityStatsOptional.isPresent()) activityStats = activityStatsOptional.get();

        List<Project> projects = organization.getProjects();

        int locPerCommitPerProject = getLocPerCommitPerProject(projects);
        int allCommits = getAllCommitsCount(projects);
        int allProjects = projects.size();
        int addedFiles = getAddedFiles(projects);

        float avgLOC = calcAverageLoC(locPerCommitPerProject, allCommits);
        activityStats.setAverageLoC(avgLOC);

        activityStats.setFilesAddedPerDay(addedFiles - activityStats.getFilesAddedPerDay());

        activityStats.setCommitsPerDay(allCommits - activityStats.getCommitsPerDay());

        activityStats.setLocAddedPerDay(locPerCommitPerProject - activityStats.getLocAddedPerDay());

        activityStats.setProjectsAddedPerDay(allProjects - activityStats.getProjectsAddedPerDay());

        activityStats.setOrganizationAnalysis(organization.getOrganizationAnalysis());

        saveActivityStats(activityStats);

        return activityStats;
    }

    private int getLocPerCommitPerProject(List<Project> projects) {
        return (int) projects.stream().mapToLong(project -> project.getCommits()
                .stream().mapToLong(Commit::getTotalLoC).sum()).sum();
    }

    private int getAllCommitsCount(List<Project> projects) {
        return projects.stream().mapToInt(project -> project.getCommits().size()).sum();
    }

    private int getAddedFiles(List<Project> projects) {
        return (int) projects.stream().mapToLong(project -> project.getCommits()
                .stream().mapToLong(Commit::getTotalFiles).sum()).sum();
    }

    private float calcAverageLoC(int locPerCommitPerProject, int allCommits) {
        return (float) locPerCommitPerProject / allCommits;
    }

    private float calcAverageFilesAdded(int addedFiles, int allCommits) {
        return (float) addedFiles / allCommits;
    }

    private void saveActivityStats(ActivityStats activityStats) {
        activityStatsRepository.save(activityStats);
    }
}
