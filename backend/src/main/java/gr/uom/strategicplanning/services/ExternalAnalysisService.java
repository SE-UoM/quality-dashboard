package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.external.strategies.CodeInspectorServiceStrategy;
import gr.uom.strategicplanning.analysis.external.strategies.PyAssessServiceStrategy;
import gr.uom.strategicplanning.enums.ProjectStatus;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.exceptions.ExternalAnalysisException;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalAnalysisService {
    Logger logger = LoggerFactory.getLogger(ExternalAnalysisService.class);
    private final PyAssessServiceStrategy pyAssessServiceStrategy;
    private final CodeInspectorServiceStrategy codeInspectorServiceStrategy;

    @Value("${services.external.pyassess.url}")
    private String PYASSESS_URL;
    @Value("${services.external.codeInspector.url}")
    private String CODE_INSPECTOR_URL;

    private String CODE_INSPECTOR_ANALYZE_HOTSPOTS = "/api/analysis/prioritize_hotspots";
    private String PYASSESS_ANALYZE_REPO = "/project_analysis";

    public ExternalAnalysisService(RestTemplate restTemplate) {
        this.pyAssessServiceStrategy = new PyAssessServiceStrategy(restTemplate);
        this.codeInspectorServiceStrategy = new CodeInspectorServiceStrategy(restTemplate);
    }


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
        Map<String, Object> params = new HashMap<>();

        params.put("endpointUrl", CODE_INSPECTOR_URL + CODE_INSPECTOR_ANALYZE_HOTSPOTS);
        params.put("gitUrl", project.getRepoUrl());
        params.put("method", HttpMethod.GET);

        logger.info("Sending Analysis Request to CodeInspector");
        codeInspectorServiceStrategy.sendRequest(params);
        project.setCodeInspectorStatus(ProjectStatus.ANALYSIS_COMPLETED);
        logger.info("CodeInspector analysis completed");

        return true;
    }

    private boolean analyzeWithPyAssess(Project project, String branch) {
        // I get this from PyAssess
        // 2023-11-10 14:09:06 2023-11-10 12:09:06.144
        // WARN 1 --- [nio-8080-exec-4] .w.s.m.s.DefaultHandlerExceptionResolver :
        // Resolved [org.springframework.web.bind.MissingServletRequestParameterException:
        // Required request parameter 'branch' for method parameter type String is not present]
        // I added the branch parameter to the method but how do we actually get the branch name?

        if (!project.hasLanguage("Python")) return false;

        // We won't get to this point if the project doesn't have Python as a language
        Map<String, Object> params = new HashMap<>();
        logger.info("Beggining analysis with PyAssess");
        params.put("endpointUrl", PYASSESS_URL + PYASSESS_ANALYZE_REPO);
        params.put("gitUrl", project.getRepoUrl());
        params.put("method", HttpMethod.POST);
        params.put("token", null);
        params.put("ciToken", null);

        logger.info("Sending request to PyAssess");
        pyAssessServiceStrategy.sendRequest(params);
        project.setPyAssessStatus(ProjectStatus.ANALYSIS_COMPLETED);
        logger.info("PyAssess analysis completed");

        return true;
    }

    public boolean analyzeWithExternalServices(Project project) {
        try {
            analyzeHotspotsWithCodeInspector(project);
            analyzeWithPyAssess(project, "master");
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
