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
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    private Long id;
    private String repoUrl;
    private int forks;
    private int stars;
    @OneToMany
    private Collection<Commit> commits;
    private int totalDevelopers;
    private int totalCommits;
    @OneToMany
    private Collection<Language> languages;
    private int totalLanguages;
    @OneToMany
    private Set<Developer> developers;

}
