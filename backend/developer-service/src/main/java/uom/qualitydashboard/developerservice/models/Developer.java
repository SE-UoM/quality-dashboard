package uom.qualitydashboard.developerservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Developer {
    @Id
    @SequenceGenerator(name = "developer_sequence", sequenceName = "developer_sequence")
    @GeneratedValue(generator = "developer_sequence")
    private Long id;

    private String name;
    private String imageURI;
    private String githubProfileURL;
}
