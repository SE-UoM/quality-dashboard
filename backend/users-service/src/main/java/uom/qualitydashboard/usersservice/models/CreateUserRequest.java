package uom.qualitydashboard.usersservice.models;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private Long organizationId;
    private String userType;
}
