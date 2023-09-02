package gr.uom.strategicplanning.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uom.strategicplanning.models.stats.TechDebtStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrganizationCodeSmellDistribution {
    @Id
    @GeneratedValue
    private Long id;
    private String severity;
    private int count;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private TechDebtStats techDebtStats;

    public boolean severityEquals(String severity) {
        return this.severity.equalsIgnoreCase(severity);
    }

    public void updateCount(int count) {
        this.count += count;
    }
}
