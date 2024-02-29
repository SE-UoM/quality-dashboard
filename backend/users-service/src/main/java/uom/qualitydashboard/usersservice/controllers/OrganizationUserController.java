package uom.qualitydashboard.usersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<OrganizationUser> createUser(@RequestBody OrganizationUser user) {
        OrganizationUser createdUser = organizationUserService.saveUser(user);

        return ResponseEntity.ok(createdUser);
    }
}
