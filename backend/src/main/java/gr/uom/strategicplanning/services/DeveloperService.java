package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Service
public class DeveloperService {

    private final GithubApiClient githubApiClient;
    private final DeveloperRepository developerRepository;
    private final ProjectService projectService;

    @Autowired
    public DeveloperService(DeveloperRepository developerRepository, @Value("${github.token}") String githubToken, ProjectService projectService) {
        this.developerRepository = developerRepository;
        this.githubApiClient = new GithubApiClient(githubToken);
        this.projectService = projectService;
    }

    public Developer populateDeveloperData(Project project, Commit commit) throws IOException {
        Long organizationId = project.getOrganization().getId();

        //toDo
        // if exists and its recent no need to call API
        String developerName = githubApiClient.fetchDeveloperNameFromCommit(project, commit);

        String developerURL = githubApiClient.fetchGithubURL(developerName);
        String developerAvatarUrl = githubApiClient.fetchAvatarUrl(developerName);

        Developer developer = findOrCreateDeveloper(developerName, project);

        developer.setTotalCommits(developer.getTotalCommits() + 1);
        developer.setGithubUrl(developerURL);
        developer.setAvatarUrl(developerAvatarUrl);
        developer.setName(developerName);
        developer.setOrganizationId(organizationId);

        project.addDeveloper(developer);

        saveDeveloper(developer);
        projectService.saveProject(project);

        return developer;
    }

    private void saveDeveloper(Developer developer) {
        developerRepository.save(developer);
    }

    private Developer findOrCreateDeveloper(String developerName, Project project) {
        Optional<Developer> developerOptional = developerRepository.findByName(developerName);

        if (developerOptional.isPresent()) return developerOptional.get();

        Developer developer = new Developer();
        developer.setName(developerName);
        developer.setGithubUrl(project.getRepoUrl());
        return developer;
    }

    public Collection<Developer> findAllByOrganizationId(Long organizationId) {
        return developerRepository.findAllByOrganizationId(organizationId);
    }
}
