package gr.uom.strategicplanning.controllers.responses.implementations;

import gr.uom.strategicplanning.controllers.responses.ResponseInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SimpleResponse implements ResponseInterface {
    private int responseCode;
    private String message;
}
