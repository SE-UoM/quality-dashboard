package uom.qualitydashboard.developerservice.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uom.qualitydashboard.developerservice.models.CreateDeveloperRequest;
import uom.qualitydashboard.developerservice.models.Developer;
import uom.qualitydashboard.developerservice.repositories.DeveloperRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeveloperService {
    private final DeveloperRepository developerRepository;

    public Collection<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    public Developer getDeveloperById(Long id) {
        Optional<Developer> developer = developerRepository.findById(id);

        if (developer.isEmpty()) throw new EntityNotFoundException("Developer not Found");

        return developer.get();
    }

    public Developer getDeveloperByName(String name) {
        Optional<Developer> developer = developerRepository.findDeveloperByName(name);

        if (developer.isEmpty()) {
            String message = String.format("Developer with name %s not found", name);
            throw new EntityNotFoundException(message);
        }

        return developer.get();
    }

    public Developer createDeveloper(CreateDeveloperRequest request) {
        String name = request.getName();
        String imageURI = request.getImageURI();
        String githubProfileURL = request.getGithubProfileURL();

        if (developerRepository.existsByGithubProfileURL(githubProfileURL)) {
            String message = String.format("Developer with github profile URL %s already exists", githubProfileURL);
            throw new EntityExistsException(message);
        }

        Developer developer = Developer.builder()
                .name(name)
                .imageURI(imageURI)
                .githubProfileURL(githubProfileURL)
                .build();

        return developerRepository.save(developer);
    }
}
