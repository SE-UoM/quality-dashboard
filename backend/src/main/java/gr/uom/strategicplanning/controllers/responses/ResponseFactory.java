package gr.uom.strategicplanning.controllers.responses;

import gr.uom.strategicplanning.controllers.responses.implementations.ErrorResponse;
import gr.uom.strategicplanning.controllers.responses.implementations.OrganizationAnalysisResponse;
import gr.uom.strategicplanning.controllers.responses.implementations.SimpleResponse;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;

public class ResponseFactory {
    public static ResponseInterface createResponse(int responseCode, String message) {
        return new SimpleResponse(responseCode, message);
    }

    public static ResponseInterface createErrorResponse(int responseCode, String message, String exceptionMessage) {
        return new ErrorResponse(responseCode, message, exceptionMessage);
    }

    public static ResponseInterface createOrganizationAnalysisResponse(OrganizationAnalysis analysis) {
        return new OrganizationAnalysisResponse(analysis);
    }

}
