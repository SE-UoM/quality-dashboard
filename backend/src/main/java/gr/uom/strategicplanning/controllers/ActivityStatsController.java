package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.repositories.ActivityStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity-stats")
public class ActivityStatsController {

    @Autowired
    private ActivityStatsRepository activityStatsRepository;


    @PostMapping
    public ResponseEntity<ActivityStats> createActivityStats(@RequestBody ActivityStats activityStats) {
        ActivityStats createdActivityStats = activityStatsRepository.save(activityStats);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActivityStats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityStats> getActivityStatsById(@PathVariable Long id) {
        ActivityStats activityStats = (ActivityStats) activityStatsRepository.findById(id).orElse(null);
        if (activityStats == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activityStats);
    }

    @GetMapping
    public ResponseEntity<Iterable<ActivityStats>> getAllActivityStats() {
        Iterable<ActivityStats> activityStats = activityStatsRepository.findAll();
        return ResponseEntity.ok(activityStats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityStats> updateActivityStats(@PathVariable Long id, @RequestBody ActivityStats updatedActivityStats) {
        ActivityStats activityStats = (ActivityStats) activityStatsRepository.findById(id).orElse(null);
        if (activityStats == null) {
            return ResponseEntity.notFound().build();
        }
        activityStats.setCommitsPerDay(updatedActivityStats.getCommitsPerDay());
        activityStats.setLocAddedPerDay(updatedActivityStats.getLocAddedPerDay());
        activityStats.setFilesAddedPerDay(updatedActivityStats.getFilesAddedPerDay());
        activityStats.setProjectsAddedPerDay(updatedActivityStats.getProjectsAddedPerDay());
        activityStats.setAverageLoC(updatedActivityStats.getAverageLoC());
        // Update other properties as needed

        ActivityStats updatedStats = activityStatsRepository.save(activityStats);
        return ResponseEntity.ok(updatedStats);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityStats(@PathVariable Long id) {
        ActivityStats activityStats = (ActivityStats) activityStatsRepository.findById(id).orElse(null);
        if (activityStats == null) {
            return ResponseEntity.notFound().build();
        }
        activityStatsRepository.delete(activityStats);
        return ResponseEntity.noContent().build();
    }
}
