package gr.uom.strategicplanning.models.domain;

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

    private String name;
    private int linesOfCode;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private GeneralStats generalStats;
}
