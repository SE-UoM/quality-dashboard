package gr.uom.strategicplanning.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AnalysisException extends ResponseStatusException {
    public AnalysisException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
    public AnalysisException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
