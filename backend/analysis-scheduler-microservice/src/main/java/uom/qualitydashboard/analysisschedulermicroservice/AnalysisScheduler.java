package uom.qualitydashboard.analysisschedulermicroservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uom.qualitydashboard.analysisschedulermicroservice.clients.SubmittedProjectsClient;
import uom.qualitydashboard.analysisschedulermicroservice.models.SubmittedProjectDTO;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalysisScheduler {
    private final int ONE_MINUTE = 60000;

    private final Lock taskLock = new ReentrantLock();
    private final SubmittedProjectsClient submittedProjectsClient;

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

                submittedProjects.forEach(System.out::println);
            } finally {
                taskLock.unlock();
            }
        }
    }
}
