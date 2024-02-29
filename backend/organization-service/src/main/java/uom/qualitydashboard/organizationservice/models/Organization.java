package uom.qualitydashboard.organizationservice.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
@Builder
public class Organization {
    @Id
    @SequenceGenerator(
            name = "organization_sequence",
            sequenceName = "organization_sequence"
    )
    @GeneratedValue(
            generator = "organization_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    private String name;
    private String city;
    private String country;
}
