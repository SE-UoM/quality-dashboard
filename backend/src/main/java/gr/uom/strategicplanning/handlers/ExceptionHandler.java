package gr.uom.strategicplanning.handlers;

import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import org.springframework.http.ResponseEntity;

public interface ExceptionHandler {
    ResponseEntity<ResponseInterface> handle(Exception e);
}
