package gr.uom.strategicplanning.handlers;

import gr.uom.strategicplanning.controllers.responses.ResponseFactory;
import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import gr.uom.strategicplanning.exceptions.AnalysisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Handles exceptions that occur during the analysis process, providing appropriate
 * responses to the client based on the type and reason of the exception.
 * <p>
 * This handler specifically deals with {@link AnalysisException} and other exceptions
 * that may arise, logging them and creating custom error responses.
 * </p>
 */

@Slf4j
public class AnalysisExceptionHandler implements ExceptionHandler {
    /**
     * A decision table mapping specific error reasons to their corresponding response
     * creation logic. This is used to generate custom error messages for known issues.
     * We populate this table with the known error reasons and their corresponding
     * response creation logic.
     *
     * <p>
     * The key is the reason for the error and the value is a function that takes
     * the status code and the reason as arguments and returns a response object.
     * This function is used to create a custom error response for the client.
     * The response object is created by calling the function with the status code
     * and the reason.
     * </p>
     */
    private static final Map<String, BiFunction<Integer, String, ResponseInterface>>
            responseFactoryDecisionTable = new HashMap<>();

    /**
     * Creates a new {@link AnalysisExceptionHandler} and populates the decision table.
     */
    public AnalysisExceptionHandler() {
        populateDecisionTable();
    }

    /**
     * Populates the decision table with specific error reasons and their corresponding
     * response creation logic.
     * <p>
     * Each entry maps an error reason to a function that takes an HTTP status code and
     * reason string, and returns a {@link ResponseInterface} with a detailed error message.
     * </p>
     */
    private void populateDecisionTable() {
        addEntryToDecisionTable(
                "Invalid github url",
                (status, reason) -> ResponseFactory.createErrorResponse(status, reason, "The provided github url is invalid. Please provide a valid github url.")
        );

        addEntryToDecisionTable(
                "The project is already being analyzed",
                (status, reason) -> ResponseFactory.createErrorResponse(status, reason, "The project is already being analyzed. Please wait for the analysis to finish.")
        );

        addEntryToDecisionTable(
                "The project needs to be reviewed by an Admin first.",
                (status, reason) -> ResponseFactory.createErrorResponse(status, reason, "The project has more commits than the threshold and needs to be reviewed by an Admin first.")
        );

        addEntryToDecisionTable(
                "The project has a lot of Commits.",
                (status, reason) -> ResponseFactory.createErrorResponse(status, reason, "The project has more commits than the threshold and needs to be reviewed by an Admin first.")
        );

        addEntryToDecisionTable(
                "All commits are already analyzed, you can see the results on the dashboard.",
                (status, reason) -> ResponseFactory.createErrorResponse(status, reason, "All commits are already analyzed. You can see the results on the dashboard.")
        );
    }

    /**
     * Handles the given exception by generating an appropriate response.
     * <p>
     * If the exception is an {@link AnalysisException}, a custom response is generated
     * based on the exception's reason and the decision table.
     * If the exception is not an {@link AnalysisException},
     * a generic internal server error response is generated.
     * </p>
     *
     * @param e the exception to handle
     * @return a {@link ResponseEntity} containing the appropriate response
     */
    @Override
    public ResponseEntity<ResponseInterface> handle(Exception e) {
        if (!(e instanceof AnalysisException)) {
            return handleUnexpectedException(e);
        }

        return handleAnalysisException((AnalysisException) e);
    }

    /**
     * Handles unexpected exceptions that are not instances of {@link AnalysisException}.
     *
     * @param e the unexpected exception to handle
     * @return a {@link ResponseEntity} containing the appropriate error response
     */
    private ResponseEntity<ResponseInterface> handleUnexpectedException(Exception e) {
        log.error(e.getMessage(), e);

        ResponseInterface response = ResponseFactory.createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Analysis failed. Please try again later or contact support.",
                e.getMessage()
        );

        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * Handles exceptions that are instances of {@link AnalysisException}.
     *
     * @param e the analysis exception to handle
     * @return a {@link ResponseEntity} containing the appropriate error response
     */
    private ResponseEntity<ResponseInterface> handleAnalysisException(AnalysisException e) {
        String reason = e.getReason();
        int responseStatus = e.getStatus().value();

        if (reason == null) {
            log.error("An AnalysisException was thrown without a specific reason.", e);
            return handleUnexpectedException(e);
        }

        ResponseInterface response = createErrorResponse(responseStatus, reason);
        return ResponseEntity.status(responseStatus).body(response);
    }

    /**
     * Creates an error response based on the provided status and reason.
     *
     * @param status the HTTP status code
     * @param reason the reason for the error
     * @return the {@link ResponseInterface} containing the detailed error message
     */
    private ResponseInterface createErrorResponse(int status, String reason) {
        BiFunction<Integer, String, ResponseInterface> responseCreator = responseFactoryDecisionTable.get(reason);

        if (responseCreator != null) {
            return responseCreator.apply(status, reason);
        }

        return ResponseFactory.createErrorResponse(
                status,
                reason,
                "An unknown error occurred. Please try again later or contact support."
        );
    }

    /**
     * Adds an entry to the decision table mapping specific error reasons to their
     * corresponding response creation logic.
     *
     * @param key   the reason for the error
     * @param value the function that creates a response for the error
     */
    private void addEntryToDecisionTable(String key, BiFunction<Integer, String, ResponseInterface> value) {
        responseFactoryDecisionTable.put(key, value);
    }
}
