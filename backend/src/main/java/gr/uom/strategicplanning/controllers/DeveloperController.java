package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.repositories.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    @Autowired
    private DeveloperRepository developerRepository;

    @GetMapping
    public ResponseEntity<List<Developer>> getAllDevelopers() {
        List<Developer> developers = developerRepository.findAll();
        return ResponseEntity.ok(developers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloperById(@PathVariable Long id) {
        Optional<Developer> developerOptional = developerRepository.findById(id);
        return developerOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Developer> createDeveloper(@RequestBody Developer developer) {
        Developer createdDeveloper = (Developer) developerRepository.save(developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeveloper);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Developer> updateDeveloper(@PathVariable Long id, @RequestBody Developer updatedDeveloper) {
        Optional<Developer> developerOptional = developerRepository.findById(id);
        if (developerOptional.isPresent()) {
            Developer existingDeveloper = developerOptional.get();
            existingDeveloper.setName(updatedDeveloper.getName());
            existingDeveloper.setGithubUrl(updatedDeveloper.getGithubUrl());
            existingDeveloper.setTotalCommits(updatedDeveloper.getTotalCommits());
            Developer savedDeveloper = (Developer) developerRepository.save(existingDeveloper);
            return ResponseEntity.ok(savedDeveloper);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        Optional<Developer> developerOptional = developerRepository.findById(id);
        if (developerOptional.isPresent()) {
            developerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
