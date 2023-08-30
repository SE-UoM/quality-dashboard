package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.DeveloperRepository;
import gr.uom.strategicplanning.repositories.ProjectRepository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
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
        String developerName = githubApiClient.fetchDeveloperName(project, commit);
        String developerUsername = githubApiClient.fetchDeveloperUsername(project, commit);

        Developer developer = findOrCreateDeveloper(developerName, project);

        developer.setTotalCommits(developer.getTotalCommits() + 1);

        //TODO: Fetch developer's username from github
        developer.setGithubUrl("www.github.com/" + developerUsername);
        developer.setName(developerName);
        developer.setProject(project);

        project.addDeveloper(developer);
        projectService.saveProject(project);
//        saveDeveloper(developer);

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
}
