package gr.uom.strategicplanning.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectAnalysis {

    @javax.persistence.Id
    @javax.persistence.GeneratedValue
    private Long id;
    @OneToOne
    private Project project;
    @OneToMany(mappedBy = "projectAnalysis")
    private Collection<String> codeSmells;
    private float technicalDebt;
    private Integer linesOfCode;
    private Integer numberOfFiles;
    @OneToMany(mappedBy = "projectAnalysis")
    private Collection<String> languages;

}
