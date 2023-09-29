package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ErrorResponse extends SimpleResponse implements ResponseInterface {
    private String exceptionMessage;

    public ErrorResponse(int responseCode, String message, String exceptionMessage) {
        super(responseCode, message);
        this.exceptionMessage = exceptionMessage;
    }
}
