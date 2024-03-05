package uom.qualitydashboard.githubanalysisservice.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class DeveloperDTO {
    private Long id;
    private String name;
    private String imageURI;
    private String githubProfileURL;
}
