package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.stats.TechDebtStats;
import gr.uom.strategicplanning.repositories.TechDebtStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/techDebtStats")
public class TechDebtStatsController {

    @Autowired
    private TechDebtStatsRepository techDebtStatsRepository;

    @GetMapping
    public ResponseEntity<List<TechDebtStats>> getAllTechDebtStats() {
        List<TechDebtStats> techDebtStatsList = techDebtStatsRepository.findAll();
        return ResponseEntity.ok(techDebtStatsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechDebtStats> getTechDebtStatsById(@PathVariable Long id) {
        Optional<TechDebtStats> techDebtStatsOptional = techDebtStatsRepository.findById(id);
        return techDebtStatsOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TechDebtStats> createTechDebtStats(@RequestBody TechDebtStats techDebtStats) {
        TechDebtStats createdTechDebtStats = (TechDebtStats) techDebtStatsRepository.save(techDebtStats);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTechDebtStats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechDebtStats> updateTechDebtStats(@PathVariable Long id, @RequestBody TechDebtStats updatedTechDebtStats) {
        Optional<TechDebtStats> techDebtStatsOptional = techDebtStatsRepository.findById(id);
        if (techDebtStatsOptional.isPresent()) {
            TechDebtStats existingTechDebtStats = techDebtStatsOptional.get();
            existingTechDebtStats.setTotalTechDebt(updatedTechDebtStats.getTotalTechDebt());
            existingTechDebtStats.setAverageTechDebt(updatedTechDebtStats.getAverageTechDebt());
            existingTechDebtStats.setProjectWithMinTechDebt(updatedTechDebtStats.getProjectWithMinTechDebt());
            existingTechDebtStats.setProjectWithMaxTechDebt(updatedTechDebtStats.getProjectWithMaxTechDebt());
            existingTechDebtStats.setAverageTechDebtPerLoC(updatedTechDebtStats.getAverageTechDebtPerLoC());
            existingTechDebtStats.setMinDebtLanguage(updatedTechDebtStats.getMinDebtLanguage());
            existingTechDebtStats.setBestTechDebtProjects(updatedTechDebtStats.getBestTechDebtProjects());
            existingTechDebtStats.setBestCodeSmellProjects(updatedTechDebtStats.getBestCodeSmellProjects());
            existingTechDebtStats.setTotalCodeSmells(updatedTechDebtStats.getTotalCodeSmells());
            existingTechDebtStats.setCodeSmells(updatedTechDebtStats.getCodeSmells());
            TechDebtStats savedTechDebtStats = (TechDebtStats) techDebtStatsRepository.save(existingTechDebtStats);
            return ResponseEntity.ok(savedTechDebtStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechDebtStats(@PathVariable Long id) {
        Optional<TechDebtStats> techDebtStatsOptional = techDebtStatsRepository.findById(id);
        if (techDebtStatsOptional.isPresent()) {
            techDebtStatsRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
