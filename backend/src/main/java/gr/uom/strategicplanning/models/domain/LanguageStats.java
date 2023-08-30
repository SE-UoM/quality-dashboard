package gr.uom.strategicplanning.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LanguageStats {
    @Id
    @GeneratedValue
    private Long id;
    private int linesOfCode;
    @ManyToOne
    @JsonIgnore
    private Project project;
    @ManyToOne
    @JsonIgnore
    private Language language;

}
