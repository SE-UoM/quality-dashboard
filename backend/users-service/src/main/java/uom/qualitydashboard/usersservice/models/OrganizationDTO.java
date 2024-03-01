package uom.qualitydashboard.usersservice.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class OrganizationDTO {
    private Long id;
    private String name;
    private String city;
    private String country;
}
