package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.responses.LanguageResponse;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/languages")
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAllLanguages() {
        List<Language> languageList = languageRepository.findAll();
        List<LanguageResponse> languageResponses = new ArrayList<>();

        for (Language language : languageList) {
            LanguageStats languageStats = new LanguageStats();
            languageStats.setLanguage(language);
            LanguageResponse languageResponse = new LanguageResponse(languageStats);
            languageResponses.add(languageResponse);
        }

        return ResponseEntity.ok(languageResponses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponse> getLanguageById(@PathVariable Long id) {
        Optional<Language> languageOptional = languageRepository.findById(id);
        LanguageStats languageStats = new LanguageStats();
        languageStats.setLanguage(languageOptional.get());
        return languageOptional.map(languageResponse -> ResponseEntity.ok(new LanguageResponse(languageStats)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LanguageResponse> createLanguage(@RequestBody Language language) {
        Language createdLanguage = languageRepository.save(language);
        LanguageStats languageStats = new LanguageStats();
        languageStats.setLanguage(createdLanguage);
        LanguageResponse languageResponse = new LanguageResponse(languageStats);
        return ResponseEntity.status(HttpStatus.CREATED).body(languageResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Language> updateLanguage(@PathVariable Long id, @RequestBody Language updatedLanguage) {
        Optional<Language> languageOptional = languageRepository.findById(id);
        if (languageOptional.isPresent()) {
            Language existingLanguage = languageOptional.get();
            existingLanguage.setName(updatedLanguage.getName());
            existingLanguage.setImageUrl(updatedLanguage.getImageUrl());
            Language savedLanguage = (Language) languageRepository.save(existingLanguage);
            return ResponseEntity.ok(savedLanguage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        Optional<Language> languageOptional = languageRepository.findById(id);
        if (languageOptional.isPresent()) {
            languageRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
