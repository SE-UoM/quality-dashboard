package gr.uom.strategicplanning.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language {
    
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String version;
    private String imageUrl;

    @ManyToOne
    private Project project;

}
