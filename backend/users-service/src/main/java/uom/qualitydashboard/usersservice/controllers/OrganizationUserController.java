package uom.qualitydashboard.usersservice.controllers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.qualitydashboard.usersservice.models.CreateUserRequest;
import uom.qualitydashboard.usersservice.models.OrganizationUser;
import uom.qualitydashboard.usersservice.services.OrganizationUserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class OrganizationUserController {
    private final OrganizationUserService organizationUserService;

    @GetMapping("/all")
    public ResponseEntity<Collection<OrganizationUser>> getAllUsers() {
        Collection<OrganizationUser> users = organizationUserService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<Collection<OrganizationUser>> getAllUsersByOrganizationId(@PathVariable(name = "organizationId") Long organizationId) {
        Collection<OrganizationUser> users = organizationUserService.getAllUsersByOrganizationId(organizationId);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<OrganizationUser> getUserById(@PathVariable(name = "userId")Long id) {
        Optional<OrganizationUser> user = organizationUserService.getUserById(id);

        if (user.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/email")
    public ResponseEntity<OrganizationUser> getUserByEmail(@RequestParam(name = "email", required = true) String email) {
        Optional<OrganizationUser> user = organizationUserService.getUserByEmail(email);

        if (user.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest userRequest) {
        // LOGIC
        try {
            OrganizationUser user = organizationUserService.createUser(userRequest);
            return ResponseEntity.ok(user);
        }

        // EXCEPTION HANDLING
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(e.getMessage());
        }
        catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
