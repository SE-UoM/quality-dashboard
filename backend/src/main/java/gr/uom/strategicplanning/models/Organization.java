package gr.uom.strategicplanning.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany
    private List<User> users;
    @OneToMany
    private List<Project> projects;
    @OneToOne
    private OrganizationAnalysis organizationAnalysis;

}
