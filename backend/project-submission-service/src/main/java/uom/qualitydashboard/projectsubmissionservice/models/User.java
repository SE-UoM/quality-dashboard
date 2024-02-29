package uom.qualitydashboard.projectsubmissionservice.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Long organizationId;
    private String roles;
    private boolean verified;
    private String userType;
}
