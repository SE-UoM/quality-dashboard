package gr.uom.strategicplanning.models.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BestPractice {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String explanation;
}
