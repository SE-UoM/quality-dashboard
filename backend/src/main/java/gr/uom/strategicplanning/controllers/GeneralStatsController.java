package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.GeneralStats;
import gr.uom.strategicplanning.repositories.GeneralStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/general-stats")
public class GeneralStatsController {

    private final GeneralStatsRepository generalStatsRepository;

    @Autowired
    public GeneralStatsController(GeneralStatsRepository generalStatsRepository) {
        this.generalStatsRepository = generalStatsRepository;
    }

    @GetMapping
    public ResponseEntity<List<GeneralStats>> getAllGeneralStats() {
        List<GeneralStats> generalStats = generalStatsRepository.findAll();
        return ResponseEntity.ok(generalStats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralStats> getGeneralStatsById(@PathVariable Long id) {
        Optional<GeneralStats> generalStatsOptional = generalStatsRepository.findById(id);
        return generalStatsOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GeneralStats> createGeneralStats(@RequestBody GeneralStats generalStats) {
        GeneralStats createdGeneralStats = (GeneralStats) generalStatsRepository.save(generalStats);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGeneralStats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralStats> updateGeneralStats(@PathVariable Long id, @RequestBody GeneralStats updatedGeneralStats) {
        Optional<GeneralStats> generalStatsOptional = generalStatsRepository.findById(id);
        if (generalStatsOptional.isPresent()) {
            GeneralStats existingGeneralStats = generalStatsOptional.get();
            existingGeneralStats.setTotalProjects(updatedGeneralStats.getTotalProjects());
            existingGeneralStats.setTotalLanguages(updatedGeneralStats.getTotalLanguages());
            existingGeneralStats.setLanguages(updatedGeneralStats.getLanguages());
            existingGeneralStats.setTopLanguages(updatedGeneralStats.getTopLanguages());
            existingGeneralStats.setTotalCommits(updatedGeneralStats.getTotalCommits());
            existingGeneralStats.setTotalFiles(updatedGeneralStats.getTotalFiles());
            existingGeneralStats.setTotalLinesOfCode(updatedGeneralStats.getTotalLinesOfCode());
            existingGeneralStats.setTotalDevs(updatedGeneralStats.getTotalDevs());
            GeneralStats savedGeneralStats = (GeneralStats) generalStatsRepository.save(existingGeneralStats);
            return ResponseEntity.ok(savedGeneralStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeneralStats(@PathVariable Long id) {
        Optional<GeneralStats> generalStatsOptional = generalStatsRepository.findById(id);
        if (generalStatsOptional.isPresent()) {
            generalStatsRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
