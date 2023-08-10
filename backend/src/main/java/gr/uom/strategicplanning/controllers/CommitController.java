package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.CommitResponse;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/commits")
public class CommitController {

    @Autowired
    private CommitRepository commitRepository;

    @GetMapping
    public ResponseEntity<List<CommitResponse>> getAllCommits() {
        List<Commit> commits = commitRepository.findAll();
        List<CommitResponse> commitResponses = commits.stream()
                .map(CommitResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commitResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommitResponse> getCommitById(@PathVariable Long id) {
        Optional<Commit> commit = commitRepository.findById(id);
        return commit.map(commitResponse -> ResponseEntity.ok(new CommitResponse(commitResponse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byProject/{project_name}")
    public ResponseEntity<CommitResponse> getCommitByProjectName(@PathVariable String project_name) {
        Optional<Commit> commit = commitRepository.findByProjectName(project_name);
        return commit.map(commitResponse -> ResponseEntity.ok(new CommitResponse(commitResponse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CommitResponse> createCommit(@RequestBody Commit commit) {
        Commit createdCommit = commitRepository.save(commit);
        CommitResponse commitResponse = new CommitResponse(createdCommit);
        return ResponseEntity.status(HttpStatus.CREATED).body(commitResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommitResponse> updateCommit(@PathVariable Long id, @RequestBody Commit updatedCommit) {
        Optional<Commit> existingCommit = commitRepository.findById(id);
        if (existingCommit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Commit commit = existingCommit.get();
        // Update commit fields

        updatedCommit = commitRepository.save(commit);
        CommitResponse commitResponse = new CommitResponse(updatedCommit);
        return ResponseEntity.ok(commitResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommit(@PathVariable Long id) {
        Optional<Commit> commit = commitRepository.findById(id);
        if (commit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        commitRepository.delete(commit.get());
        return ResponseEntity.noContent().build();
    }
}
