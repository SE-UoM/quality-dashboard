package gr.uom.strategicplanning.controllers.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter @Setter
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String password;
    private Long organizationId;
}
