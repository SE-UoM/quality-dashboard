package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.domain.BestPractice;
import gr.uom.strategicplanning.services.BestPracticesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/best-practices")
public class BestPracticesController {
    private final BestPracticesService bestPracticesService;

    @Autowired
    public BestPracticesController(BestPracticesService bestPracticesService) {
        this.bestPracticesService = bestPracticesService;
    }

    @GetMapping
    public ResponseEntity<Collection<BestPractice>> getAllBestPractices() {
        Collection<BestPractice> bestPractices = bestPracticesService.getAllBestPractices();

        return ResponseEntity.ok(bestPractices);
    }

    @GetMapping("/random")
    public ResponseEntity<BestPractice> getRandomBestPractice() {
        BestPractice bestPractice = bestPracticesService.getRandomBestPractice();

        if (bestPractice == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(bestPractice);
    }
}
