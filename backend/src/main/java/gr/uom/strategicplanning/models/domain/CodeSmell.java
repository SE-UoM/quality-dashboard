package gr.uom.strategicplanning.models.domain;

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
public class CodeSmell {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String severityLevel;
    private int line;
    private int remediationTime;
}
