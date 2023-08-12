package gr.uom.strategicplanning.models.domain;

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
    private Project project;
    @ManyToOne
    private Language language;

}
