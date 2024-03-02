package uom.qualitydashboard.projectsubmissionservice.controllers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.qualitydashboard.projectsubmissionservice.models.ProjectSubmissionRequest;
import uom.qualitydashboard.projectsubmissionservice.services.SubmittedProjectService;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/projects/submissions")
@RequiredArgsConstructor
public class SubmittedProjectController {
    private final SubmittedProjectService submittedProjectService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllSubmissions() {
        return ResponseEntity.ok(submittedProjectService.getAllSubmissions());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getSubmissionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(submittedProjectService.getSubmissionsByUserId(userId));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<Collection<?>> getSubmissionsByOrganizationId(@PathVariable Long organizationId) {
        return ResponseEntity.ok(submittedProjectService.getSubmissionsByOrganizationId(organizationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.of(submittedProjectService.getSubmissionById(id));
    }

    @PostMapping("/new")
    public ResponseEntity<?> submitProject(@RequestBody ProjectSubmissionRequest projectSubmissionRequest) {
        try {
            return ResponseEntity.ok(submittedProjectService.submitProject(projectSubmissionRequest));
        }
        catch (IllegalArgumentException e) {
            String message = e.getMessage();

            if (!message.equals("Invalid Github repository URL")) e.printStackTrace();

            return ResponseEntity.badRequest().body(message);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
