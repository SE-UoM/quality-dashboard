package uom.qualitydashboard.developerservice.models;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class CreateDeveloperRequest {
    private String name;
    private String imageURI;
    private String githubProfileURL;
}
