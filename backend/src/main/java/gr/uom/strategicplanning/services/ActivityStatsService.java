package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.repositories.ActivityStatsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityStatsService {

    private ActivityStatsRepository activityStatsRepository;

    public ActivityStatsService(ActivityStatsRepository activityStatsRepository) {
        this.activityStatsRepository = activityStatsRepository;
    }

    public ActivityStats getActivityStats(Organization organization) {
        ActivityStats activityStats = new ActivityStats();
        Optional<ActivityStats> activityStatsOptional = activityStatsRepository.findByOrganization(organization);
        if (activityStatsOptional.isPresent()) {
            activityStats = activityStatsOptional.get();
        }

        int locPerCommitPerProject = (int) organization.getProjects().stream().mapToLong(project -> project.getCommits()
                .stream().mapToLong(Commit::getTotalLoC).sum()).sum();
        int allCommits = organization.getProjects().stream().mapToInt(project -> project.getCommits().size()).sum();

        int allProjects = organization.getProjects().size();

        int addedFiles = (int) organization.getProjects().stream().mapToLong(project -> project.getCommits()
                .stream().mapToLong(Commit::getTotalFiles).sum()).sum();

        activityStats.setAverageLoC((float) locPerCommitPerProject / allCommits);

        activityStats.setFilesAddedPerDay(addedFiles - activityStats.getFilesAddedPerDay());

        activityStats.setCommitsPerDay(allCommits - activityStats.getCommitsPerDay());

        activityStats.setLocAddedPerDay(locPerCommitPerProject - activityStats.getLocAddedPerDay());

        activityStats.setProjectsAddedPerDay(allProjects - activityStats.getProjectsAddedPerDay());

        return activityStats;
    }
}
