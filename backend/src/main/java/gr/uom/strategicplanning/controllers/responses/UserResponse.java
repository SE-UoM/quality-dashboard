package gr.uom.strategicplanning.controllers.responses;


import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {

        private Long id;
        private String name;
        private String email;
        private String password;
        private String roles;
        private boolean verified;
        private String organization;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.verified = user.isVerified();
        if(user.getOrganization() != null){
            this.organization = user.getOrganization().getName();
        }
    }
}
