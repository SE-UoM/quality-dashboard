package gr.uom.strategicplanning.models.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Language {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String imageUrl;

    public Language(String name) {
        this.name = name;
    }

    public boolean is(String language) {
        return this.name.equals(language);
    }
}
