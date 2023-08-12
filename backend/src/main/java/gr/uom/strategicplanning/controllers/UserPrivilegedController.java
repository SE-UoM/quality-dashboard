package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.UserResponse;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.services.UserPrivilegedService;
import gr.uom.strategicplanning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/api/admin")
public class UserPrivilegedController {

    @Autowired
    private UserPrivilegedService userPrivilegedService;

    @Autowired
    private UserService userService;

    @PutMapping("/verify")
    User verifyUser(@RequestParam String email){
        return userPrivilegedService.verifyUser(email);
    }

    @PutMapping("/authorize")
    User givePrivilegeToUser(@RequestParam String email){
        return userPrivilegedService.givePrivilegeToUser(email);
    }

    @GetMapping("/user/organization/{id}")
    List<UserResponse> getUsersByOrganizationId(@PathVariable Long id){
        Optional<List<UserResponse>> userResponses = userService.getUsersByOrganizationId(id);

        if(userResponses.isEmpty())
            throw new RuntimeException("No users found");

        return userResponses.get();
    }
}
