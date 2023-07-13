package gr.uom.strategicplanning.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private String roles;
    private boolean verified;
    @ManyToOne
    private Organization organization;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
