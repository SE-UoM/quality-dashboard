package gr.uom.strategicplanning.models.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProjectLanguage {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int linesOfCode;

    @ManyToOne
    private Project project;

    // Equals is based on name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectLanguage)) return false;
        ProjectLanguage that = (ProjectLanguage) o;
        return getName().equals(that.getName());
    }

    public boolean isTheSame(OrganizationLanguage other) {
        return this.name.equals(other.getName());
    }
}
