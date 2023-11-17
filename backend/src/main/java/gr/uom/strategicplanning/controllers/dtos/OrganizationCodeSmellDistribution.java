package gr.uom.strategicplanning.controllers.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrganizationCodeSmellDistribution {
    private String severity;
    private int count;
}