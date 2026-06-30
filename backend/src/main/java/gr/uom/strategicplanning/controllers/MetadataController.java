package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Exposes project quality metadata to external systems in a standards-aligned way.
 *
 *   GET /api/metadata/dcat           -> the whole catalogue as a DCAT Catalog (JSON-LD)
 *   GET /api/metadata/projects/{id}  -> a single project as schema.org SoftwareSourceCode (JSON-LD)
 *
 * Both responses are valid JSON-LD (W3C) and use the DCAT (W3C) and schema.org
 * vocabularies, so the application can legitimately claim alignment with the
 * JSON-LD and DCAT interoperability standards. Read-only, no DB changes required.
 */
@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    @Autowired
    private ProjectRepository projectRepository;

    // Relative identifier base for each dataset/project.
    private static final String BASE = "/api/metadata/projects/";

    @GetMapping(value = "/dcat", produces = "application/ld+json")
    public ResponseEntity<Map<String, Object>> getCatalog() {
        List<Map<String, Object>> datasets = new ArrayList<>();
        for (Project p : projectRepository.findAll()) {
            datasets.add(toDataset(p));
        }

        Map<String, Object> catalog = new LinkedHashMap<>();
        catalog.put("@context", jsonLdContext());
        catalog.put("@type", "dcat:Catalog");
        catalog.put("dct:title", "Quality Dashboard - Software Quality Catalogue");
        catalog.put("dct:description",
                "Catalogue of software projects analysed by the Quality Dashboard, "
                        + "including code-quality metrics and repository statistics.");
        catalog.put("dcat:dataset", datasets);
        return ResponseEntity.ok(catalog);
    }

    @GetMapping(value = "/projects/{id}", produces = "application/ld+json")
    public ResponseEntity<Map<String, Object>> getProjectMetadata(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(p -> ResponseEntity.ok(toSoftwareSourceCode(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // --- helpers -------------------------------------------------------------

    private Map<String, String> jsonLdContext() {
        Map<String, String> ctx = new LinkedHashMap<>();
        ctx.put("dcat", "http://www.w3.org/ns/dcat#");
        ctx.put("dct", "http://purl.org/dc/terms/");
        ctx.put("schema", "https://schema.org/");
        return ctx;
    }

    /** One project as a dcat:Dataset (used inside the catalogue). */
    private Map<String, Object> toDataset(Project p) {
        Map<String, Object> ds = new LinkedHashMap<>();
        ds.put("@type", "dcat:Dataset");
        ds.put("@id", BASE + p.getId());
        ds.put("dct:title", p.getName());
        ds.put("dct:description", p.getProjectDescription());
        ds.put("dct:creator", p.getOwnerName());
        ds.put("dct:issued", p.getCreatedAt());
        ds.put("dcat:landingPage", p.getRepoUrl());
        ds.put("schema:codeRepository", p.getRepoUrl());
        ds.put("schema:programmingLanguage", p.getMainLang());
        ds.put("schema:additionalProperty", qualityMetrics(p));
        return ds;
    }

    /** One project as a standalone schema.org SoftwareSourceCode. */
    private Map<String, Object> toSoftwareSourceCode(Project p) {
        Map<String, Object> obj = new LinkedHashMap<>();
        obj.put("@context", "https://schema.org/");
        obj.put("@type", "SoftwareSourceCode");
        obj.put("@id", BASE + p.getId());
        obj.put("name", p.getName());
        obj.put("description", p.getProjectDescription());
        obj.put("author", p.getOwnerName());
        obj.put("dateCreated", p.getCreatedAt());
        obj.put("codeRepository", p.getRepoUrl());
        obj.put("programmingLanguage", p.getMainLang());
        obj.put("additionalProperty", qualityMetrics(p));
        return obj;
    }

    private List<Map<String, Object>> qualityMetrics(Project p) {
        List<Map<String, Object>> metrics = new ArrayList<>();
        metrics.add(propertyValue("stars", p.getStars()));
        metrics.add(propertyValue("forks", p.getForks()));
        metrics.add(propertyValue("totalCommits", p.getTotalCommits()));
        metrics.add(propertyValue("totalDevelopers", p.getTotalDevelopers()));

        ProjectStats stats = p.getProjectStats();
        if (stats != null) {
            metrics.add(propertyValue("linesOfCode", stats.getTotalLoC()));
            metrics.add(propertyValue("totalFiles", stats.getTotalFiles()));
            metrics.add(propertyValue("totalCodeSmells", stats.getTotalCodeSmells()));
            metrics.add(propertyValue("technicalDebt", stats.getTechDebt()));
            metrics.add(propertyValue("technicalDebtPerLoC", stats.getTechDebtPerLoC()));
        }
        return metrics;
    }

    private Map<String, Object> propertyValue(String name, Object value) {
        Map<String, Object> pv = new LinkedHashMap<>();
        pv.put("@type", "schema:PropertyValue");
        pv.put("schema:name", name);
        pv.put("schema:value", value);
        return pv;
    }
}
