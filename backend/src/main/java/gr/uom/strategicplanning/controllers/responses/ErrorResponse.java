package gr.uom.strategicplanning.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ErrorResponse {
    private int responseCode;
    private String message;
    private String exceptionMessage;
}
