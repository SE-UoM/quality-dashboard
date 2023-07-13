package gr.uom.strategicplanning.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    private Collection<Language> languages = new ArrayList<>();
    private int totalLanguages;
    @OneToMany
    private Set<Developer> developers = new HashSet<>();

}
