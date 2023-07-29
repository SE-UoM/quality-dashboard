package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/languages")
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @GetMapping
    public ResponseEntity<List<Language>> getAllLanguages() {
        List<Language> languages = languageRepository.findAll();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Language> getLanguageById(@PathVariable Long id) {
        Optional<Language> languageOptional = languageRepository.findById(id);
        return languageOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Language> createLanguage(@RequestBody Language language) {
        Language createdLanguage = (Language) languageRepository.save(language);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLanguage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Language> updateLanguage(@PathVariable Long id, @RequestBody Language updatedLanguage) {
        Optional<Language> languageOptional = languageRepository.findById(id);
        if (languageOptional.isPresent()) {
            Language existingLanguage = languageOptional.get();
            existingLanguage.setName(updatedLanguage.getName());
            existingLanguage.setVersion(updatedLanguage.getVersion());
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
