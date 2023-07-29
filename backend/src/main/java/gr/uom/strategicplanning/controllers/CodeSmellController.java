package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.domain.CodeSmell;
import gr.uom.strategicplanning.repositories.CodeSmellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/code-smells")
public class CodeSmellController {

    @Autowired
    private CodeSmellRepository codeSmellRepository;
    
    @GetMapping
    public ResponseEntity<List<CodeSmell>> getAllCodeSmells() {
        List<CodeSmell> codeSmells = codeSmellRepository.findAll();
        return ResponseEntity.ok(codeSmells);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodeSmell> getCodeSmellById(@PathVariable Long id) {
        Optional<CodeSmell> codeSmell = codeSmellRepository.findById(id);
        return codeSmell.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CodeSmell> createCodeSmell(@RequestBody CodeSmell codeSmell) {
        CodeSmell createdCodeSmell = (CodeSmell) codeSmellRepository.save(codeSmell);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCodeSmell);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CodeSmell> updateCodeSmell(@PathVariable Long id, @RequestBody CodeSmell updatedCodeSmell) {
        Optional<CodeSmell> existingCodeSmell = codeSmellRepository.findById(id);
        if (existingCodeSmell.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CodeSmell codeSmell = existingCodeSmell.get();
        codeSmell.setName(updatedCodeSmell.getName());
        codeSmell.setSeverityLevel(updatedCodeSmell.getSeverityLevel());
        codeSmell.setLine(updatedCodeSmell.getLine());
        codeSmell.setRemediationTime(updatedCodeSmell.getRemediationTime());
        codeSmell.setDescription(updatedCodeSmell.getDescription());

        CodeSmell updatedSmell = (CodeSmell) codeSmellRepository.save(codeSmell);
        return ResponseEntity.ok(updatedSmell);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodeSmell(@PathVariable Long id) {
        Optional<CodeSmell> codeSmell = codeSmellRepository.findById(id);
        if (codeSmell.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        codeSmellRepository.delete(codeSmell.get());
        return ResponseEntity.noContent().build();
    }
}
