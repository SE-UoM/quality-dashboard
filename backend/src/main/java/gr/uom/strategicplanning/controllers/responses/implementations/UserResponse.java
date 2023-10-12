package gr.uom.strategicplanning.controllers.responses.implementations;


import gr.uom.strategicplanning.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {

        private Long id;
        private String name;
        private String email;
        private String roles;
        private boolean verified;
        private String organizationName;
        private Long organizationId;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.verified = user.isVerified();
        if(user.getOrganization() != null){
            this.organizationName = user.getOrganization().getName();
            this.organizationId = user.getOrganization().getId();
        }
    }

    public static List<UserResponse> convertToUserResponseList(List<User> users) {
        List<UserResponse> userResponses = new java.util.ArrayList<>();
        for (User user : users) {
            userResponses.add(new UserResponse(user));
        }
        return userResponses;
    }
}
