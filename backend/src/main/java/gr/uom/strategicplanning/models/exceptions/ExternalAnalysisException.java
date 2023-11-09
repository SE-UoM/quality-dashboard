package gr.uom.strategicplanning.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ExternalAnalysisException extends RuntimeException{
    private String externalServiceName;
    private String msg;

    public ExternalAnalysisException(String serviceName) {
        super("Analysis with external service: " + serviceName + " failed!");
        this.externalServiceName = serviceName;
    }
}
