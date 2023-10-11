package gr.uom.strategicplanning.controllers.responses;

import gr.uom.strategicplanning.controllers.responses.implementations.*;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;

import java.util.Collection;

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

    public static ResponseInterface createLanguageDistributionResponse(int totalLanguages, Collection<LanguageResponse> languageDistribution) {
        return new LanguageDistributionResponse(totalLanguages, languageDistribution);
    }
}
