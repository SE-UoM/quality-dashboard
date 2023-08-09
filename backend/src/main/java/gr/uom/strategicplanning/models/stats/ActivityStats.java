package gr.uom.strategicplanning.models.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityStats {
    
    @Id
    @GeneratedValue
    private Long id;
    private float commitsPerDay;
    private float locAddedPerDay;
    private float filesAddedPerDay;
    private float projectsAddedPerDay;
    private float averageLoC;
    
}
