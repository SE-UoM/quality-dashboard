package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.UserResponse;
import gr.uom.strategicplanning.models.users.User;
import gr.uom.strategicplanning.services.UserPrivilegedService;
import gr.uom.strategicplanning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    ResponseEntity<String> verifyUser(@RequestParam String email){
        try {
            User verifiedUser = userPrivilegedService.verifyUser(email);
            return ResponseEntity.ok("User with email " + verifiedUser.getEmail() + " is verified");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @PutMapping("/authorize")
    ResponseEntity<String> givePrivilegeToUser(@RequestParam String email){
        try {
            User userWithPrivilege = userPrivilegedService.givePrivilegeToUser(email);
            return ResponseEntity.ok("User with email " + userWithPrivilege.getEmail() + " is privileged");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @GetMapping("/user/organization/{id}")
    List<UserResponse> getUsersByOrganizationId(@PathVariable Long id){
        Optional<List<UserResponse>> userResponses = userService.getUsersByOrganizationId(id);

        if(userResponses.isEmpty())
            throw new RuntimeException("No users found");

        return userResponses.get();
    }
}
