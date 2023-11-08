package gr.uom.strategicplanning.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProjectCodeSmellDistribution {
    @Id
    @GeneratedValue
    private Long id;
    private String codeSmell;
    private int count;

    @ManyToOne
    @JsonIgnore
    private ProjectStats projectStats;
}
