package gr.uom.strategicplanning.models.domain;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.stats.GeneralStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OrganizationLanguage {
    @Id
    @GeneratedValue
    private Long id;

    private String name = "";
    private int linesOfCode;

    @ManyToOne
    private OrganizationAnalysis organizationAnalysis;

    // Equals is based on name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganizationLanguage)) return false;
        OrganizationLanguage that = (OrganizationLanguage) o;
        return getName().equals(that.getName());
    }
}
