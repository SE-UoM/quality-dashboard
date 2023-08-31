package gr.uom.strategicplanning.models.analyses;

import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.OrganizationLanguage;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ActivityStats;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationAnalysis {
    public static final int COMMITS_THRESHOLD = 50;
    public static final int TOP_LANGUAGES_COUNT = 3;

    @Id
    @GeneratedValue
    private Long id;
    private String orgName;
    private Date analysisDate;
    @OneToOne(cascade = CascadeType.PERSIST)
    private GeneralStats generalStats = new GeneralStats(this);
    @OneToOne(cascade = CascadeType.PERSIST)
    private TechDebtStats techDebtStats = new TechDebtStats(this);
    @OneToOne(cascade = CascadeType.PERSIST)
    private ActivityStats activityStats = new ActivityStats(this);
    @OneToOne(cascade = CascadeType.PERSIST)
    private Project mostStarredProject;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Project mostForkedProject;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "organizationAnalysis")
    private Collection<OrganizationLanguage> languages;

    @ManyToMany
    private Map<Integer, OrganizationLanguage> topLanguages;

    @OneToOne
    private Organization organization;

    public void addLanguage(OrganizationLanguage language) {
        boolean languageDoesNotExist = !languageExists(language);

        if (languageDoesNotExist) languages.add(language);
    }

    public boolean languageExists(OrganizationLanguage language) {
        return languages.contains(language);
    }

    public Map<Integer, OrganizationLanguage> findTopLanguages() {
        // Create a comparator to sort languages by lines of code in descending order
        Comparator<OrganizationLanguage> linesOfCodeComparator = (l1, l2) ->
                Integer.compare(l2.getLinesOfCode(), l1.getLinesOfCode());

        // Sort the languages using the comparator
        List<OrganizationLanguage> sortedLanguages = languages.stream()
                .sorted(linesOfCodeComparator)
                .collect(Collectors.toList());

        // Clear the existing topLanguages map
        topLanguages.clear();

        // Get the specified number of top languages or fewer if there are less than that number of languages
        int topCount = Math.min(TOP_LANGUAGES_COUNT, sortedLanguages.size());
        for (int i = 0; i < topCount; i++) {
            topLanguages.put(i + 1, sortedLanguages.get(i));
        }

        // Return the top languages
        return this.topLanguages;
    }

}