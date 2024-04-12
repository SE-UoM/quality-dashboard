package gr.uom.strategicplanning.controllers.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class ResetPasswordRequest {
    String token;
    String password;
    Long uid;
}
