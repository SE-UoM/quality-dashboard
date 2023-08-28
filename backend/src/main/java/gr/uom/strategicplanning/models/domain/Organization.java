package gr.uom.strategicplanning.models.domain;

import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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
    @OneToMany(mappedBy = "organization")
    private List<User> users = new ArrayList<>();
    @OneToMany(mappedBy = "organization")
    private List<Project> projects = new ArrayList<>();
    @OneToOne
    private OrganizationAnalysis organizationAnalysis = new OrganizationAnalysis();

    public void addUser(User user){
        users.add(user);
    }

    public void addProject(Project project) {
        projects.add(project);
    }
}
