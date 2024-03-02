package uom.qualitydashboard.analysisschedulermicroservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uom.qualitydashboard.analysisschedulermicroservice.clients.GithubAnalysisMicroserviceClient;
import uom.qualitydashboard.analysisschedulermicroservice.clients.SubmittedProjectsClient;
import uom.qualitydashboard.analysisschedulermicroservice.models.GithubAnalysisDTO;
import uom.qualitydashboard.analysisschedulermicroservice.models.SubmittedProjectDTO;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalysisScheduler {
    private final int ONE_MINUTE = 60000;

    private final Lock taskLock = new ReentrantLock();
    private final SubmittedProjectsClient submittedProjectsClient;
    private final GithubAnalysisMicroserviceClient githubAnalysisMicroserviceClient;

    @Value("${analysis.organization}")
    private Long organizationId;

    @Scheduled(fixedRate = ONE_MINUTE)
    public void scheduleAnalysis() {
        runWithLock();
    }

    private void runWithLock() {
        if (taskLock.tryLock()) {
            try {
                log.info("Scheduling analysis for organization: {}", organizationId);
                Collection<SubmittedProjectDTO> submittedProjects = submittedProjectsClient.getSubmissionsByOrganizationId(organizationId);

                System.out.println("--- STARTING ANALYSIS ---");
                int totalProjects = submittedProjects.size();
                System.out.println("Total projects: " + totalProjects);

                for (SubmittedProjectDTO project : submittedProjects) {
                    // First see if already have any analyses
                    Long pid = project.getId();
                    Collection<GithubAnalysisDTO> analyses = githubAnalysisMicroserviceClient.getAnalysisByProjectId(pid);

                    // Find the latest analysis using the timestamp
                    Optional<GithubAnalysisDTO> latestAnalysis =  analyses
                            .stream()
                            .max((analysis1, analysis2) -> analysis1
                                            .getLastAnalysisDate()
                                            .compareTo(analysis2.getLastAnalysisDate())
                            );

                    // If there are no analyses or the latest analysis is older than 1 day, start a new analysis
                    boolean shouldStartNewAnalysis = latestAnalysis.isEmpty() || latestAnalysisOlderThanOneDay(latestAnalysis.get());
                    if (shouldStartNewAnalysis) {
                        log.info("Starting analysis for project: {}", project.getId());
                        GithubAnalysisDTO analysis = githubAnalysisMicroserviceClient.startAnalysis(pid);

                        System.out.println("Result:" + System.lineSeparator() + analysis);
                    }
                }
            } finally {
                taskLock.unlock();
            }
        }
    }

    private boolean latestAnalysisOlderThanOneDay(GithubAnalysisDTO latestAnalysis) {
        // Check if the latest analysis is older than 1 day
        return latestAnalysis.getLastAnalysisDate().getTime() < System.currentTimeMillis() - ONE_MINUTE;
    }
}
