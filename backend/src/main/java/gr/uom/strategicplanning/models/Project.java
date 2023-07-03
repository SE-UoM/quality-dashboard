package gr.uom.strategicplanning.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String owner;
    @OneToMany(mappedBy = "project")
    private Collection<Developer> contributors;
    @OneToMany(mappedBy = "project")
    private Collection<Commit> commits;
    private Integer forks;
    private Integer stars;
    private String gitUrl;

}
