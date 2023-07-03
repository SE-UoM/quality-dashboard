package gr.uom.strategicplanning.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Organization {

    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy = "organization")
    private Collection<User> users;
    @OneToMany(mappedBy = "organization")
    private Collection<Project> projects;
    @OneToMany(mappedBy = "organization")
    private Collection<ProjectAnalysis> projectAnalyses;

}
