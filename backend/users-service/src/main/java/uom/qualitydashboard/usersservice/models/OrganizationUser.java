package uom.qualitydashboard.usersservice.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class OrganizationUser {
    @Id
    @SequenceGenerator(
            name = "organization_user_sequence",
            sequenceName = "organization_user_sequence"
    )
    @GeneratedValue(
            generator = "organization_user_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    private String name;
    private String email;
    private String password;
    private Long organizationId;
    private String userType;

    @Builder.Default
    private String roles = "SIMPLE_USER";
    @Builder.Default
    private boolean verified = false;
}
