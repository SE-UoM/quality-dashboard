package uom.qualitydashboard.developerservice.controllers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.qualitydashboard.developerservice.models.CreateDeveloperRequest;
import uom.qualitydashboard.developerservice.models.Developer;
import uom.qualitydashboard.developerservice.services.DeveloperService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/developers")
public class DeveloperController {
    private final DeveloperService developerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDevelopers() {
        Collection<?> developers = developerService.getAllDevelopers();

        return ResponseEntity.ok(developers);
    }

    @GetMapping("/name")
    public ResponseEntity<?> getDeveloperByName(@RequestParam(name = "key") String name) {
        try {
            Developer developer = developerService.getDeveloperByName(name);
            return ResponseEntity.ok(developer);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            e.printStackTrace();

            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/github-profile-url")
    public ResponseEntity<?> getDeveloperByGithubProfileURL(@RequestParam(name = "key") String githubProfileURL) {
        try {
            Developer developer = developerService.getDeveloperByGithubProfileURL(githubProfileURL);
            return ResponseEntity.ok(developer);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            e.printStackTrace();

            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{developerId}")
    public ResponseEntity<?> getDeveloperById(@PathVariable Long developerId) {
        try {
            Developer developer = developerService.getDeveloperById(developerId);
            return ResponseEntity.ok(developer);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            e.printStackTrace();

            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDeveloper(@RequestBody CreateDeveloperRequest developer) {
        try {
            Developer newDeveloper = developerService.createDeveloper(developer);
            return ResponseEntity.ok(newDeveloper);
        }
        catch (EntityExistsException e) {
            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        catch (Exception e) {
            e.printStackTrace();

            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
