package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.strategies.PyAssessServiceStrategy;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.exceptions.ExternalAnalysisException;
import gr.uom.strategicplanning.models.external.CodeInspectorProjectStats;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ExternalAnalysisService {
    Logger logger = LoggerFactory.getLogger(ExternalAnalysisService.class);
    @Autowired
    private PyAssessService pyAssessService;
    @Autowired
    private CodeInspectorService codeInspectorService;

    // Simulate analysis (just to make sure language finding works)
    private void fakeAnalysis(String name, int waitTime) {
        // Simulate analysis with CodeInspector (wait and print "waiting for CodeInspector")
        try {
            Thread.sleep(waitTime);
            System.out.println("--------- Simulated Analysis with " + name + " ---------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean analyzeHotspotsWithCodeInspector(Project project) {
        logger.info("Beggining analysis with CodeInspector");

        // Analyze with CodeInspector
        Map<String, Object> codeInspectorResponse = codeInspectorService.analyzeHotspots(project);
        codeInspectorService.updateHotspotStats(project, codeInspectorResponse);

        // Update organization level stats
        Organization org = project.getOrganization();
        CodeInspectorProjectStats projectStats = project.getCodeInspectorProjectStats();
        codeInspectorService.updateOrganizationHotspotStats(org, projectStats);

        // We are done with the analysis
        project.setCodeInspectorStatus(ProjectStatus.ANALYSIS_COMPLETED);
        logger.info("CodeInspector analysis completed");

        return true;
    }

    private boolean analyzeWithPyAssess(Project project) {
        logger.info("Beggining analysis with PyAssess");

        int analysisStatus = pyAssessService.sendAnalysisRequest(project);
        // We get here if the project is a Python project and has not been analyzed before

        Optional<Map<String, Object>> analysisResults = pyAssessService.getAnalysisResults(project);

        Map<String, Object> analysisItems = analysisResults.get();
        pyAssessService.updateProjectStats(project, analysisItems);

        // Update organization level stats
        Organization org = project.getOrganization();
        pyAssessService.updateOrganizationStats(org);

        return true;
    }

    public boolean analyzeWithExternalServices(Project project) {
        log.info("AnalysisService - analyzeProject - External Analysis Started");

        try {
            analyzeHotspotsWithCodeInspector(project);
            analyzeWithPyAssess(project);
        }
        catch (Exception e) {
            handleAnalysisException(e, project);
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void handleAnalysisException(Exception e, Project project) {
        if (e instanceof ExternalAnalysisException) {
            ExternalAnalysisException casted = (ExternalAnalysisException) e;

            String serviceName = casted.getExternalServiceName();

            if (serviceName.equalsIgnoreCase("CodeInspector")) {
                project.setCodeInspectorStatus(ProjectStatus.ANALYSIS_FAILED);
                logger.error("CodeInspector analysis failed");
            }

            if (serviceName.equalsIgnoreCase("PyAssess")) {
                project.setPyAssessStatus(ProjectStatus.ANALYSIS_FAILED);
                logger.error("PyAssess analysis failed");
            }
        }

        logger.error("External analysis failed");
    }

}
