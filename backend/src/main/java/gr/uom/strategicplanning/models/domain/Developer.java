package gr.uom.strategicplanning.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Developer {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String githubUrl;
    private int totalCommits;
    @ManyToOne
    private Project project;

}
