package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.BestPractice;
import gr.uom.strategicplanning.repositories.BestPracticesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class BestPracticesService {
    private final BestPracticesRepository bestPracticesRepository;

    @Autowired
    public BestPracticesService(BestPracticesRepository bestPracticesRepository) {
        this.bestPracticesRepository = bestPracticesRepository;
    }

    public List<BestPractice> getAllBestPractices() {
        return bestPracticesRepository.findAll();
    }

    public BestPractice getRandomBestPractice() {
        Collection<BestPractice> bestPractices = bestPracticesRepository.findAll();

        if (bestPractices.isEmpty()) return null;

        int randomIndex = (int) (Math.random() * bestPractices.size());

        return (BestPractice) bestPractices.toArray()[randomIndex];
    }
}
