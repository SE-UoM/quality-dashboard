package uom.qualitydashboard.analysisschedulermicroservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class AnalysisScheduler {
    private final int ONE_MINUTE = 60000;

    private final Lock taskLock = new ReentrantLock();

    @Value("${analysis.organization}")
    private Long organizationId;

    @Scheduled(fixedRate = ONE_MINUTE)
    public void scheduleAnalysis() {
        log.info("Running scheduled analysis for organization: {}", organizationId);
    }
}
