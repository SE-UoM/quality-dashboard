package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.Commit;
import gr.uom.strategicplanning.repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commits")
public class CommitController {

    @Autowired
    private CommitRepository commitRepository;

    @GetMapping
    public ResponseEntity<List<Commit>> getAllCommits() {
        List<Commit> commits = commitRepository.findAll();
        return ResponseEntity.ok(commits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commit> getCommitById(@PathVariable Long id) {
        Optional<Commit> commit = commitRepository.findById(id);
        return commit.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Commit> createCommit(@RequestBody Commit commit) {
        Commit createdCommit = (Commit) commitRepository.save(commit);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Commit> updateCommit(@PathVariable Long id, @RequestBody Commit updatedCommit) {
        Optional<Commit> existingCommit = commitRepository.findById(id);
        if (existingCommit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Commit commit = existingCommit.get();
        commit.setHash(updatedCommit.getHash());
        commit.setDeveloper(updatedCommit.getDeveloper());
        commit.setCommitDate(updatedCommit.getCommitDate());
        commit.setCodeSmells(updatedCommit.getCodeSmells());
        commit.setTechnicalDebt(updatedCommit.getTechnicalDebt());
        commit.setTotalFiles(updatedCommit.getTotalFiles());
        commit.setTotalLoC(updatedCommit.getTotalLoC());
        commit.setTotalCodeSmells(updatedCommit.getTotalCodeSmells());
        commit.setTechDebtPerLoC(updatedCommit.getTechDebtPerLoC());
        commit.setLanguages(updatedCommit.getLanguages());
        commit.setTotalLanguages(updatedCommit.getTotalLanguages());

        updatedCommit = (Commit) commitRepository.save(commit);
        return ResponseEntity.ok(updatedCommit);
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
