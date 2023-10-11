package gr.uom.strategicplanning.analysis.external.strategies;

import java.util.Map;

/**
 * This interface represents a strategy for interacting with an external service.
 */
public interface ExternalServiceStrategy {
    /**
     * Constructs the URL based on the provided parameters.
     *
     * @param params A map of parameters used to construct the URL.
     * @return The constructed URL as a string.
     */
    String constructUrl(Map<String, String> params);

    /**
     * Sends a request to the external service using the provided parameters.
     *
     * @param params A map of parameters needed for the request.
     */
    void sendRequest(Map<String, String> params);
}
