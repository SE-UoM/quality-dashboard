package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.DeveloperRepository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DeveloperService {

    private GithubApiClient githubApiClient = new GithubApiClient();
    private DeveloperRepository developerRepository;

    @Autowired
    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    public Developer populateDeveloperData(Project project, Commit commit) throws IOException {
        String developerName = githubApiClient.fetchDeveloperName(project, commit);

        Developer developer = findOrCreateDeveloper(developerName, project);

        developer.setTotalCommits(developer.getTotalCommits() + 1);

        String githubUsername = developerName;
        String githubUrl = generateGithubUrl(githubUsername);
        developer.setGithubUrl(githubUrl);
        developer.setName(githubUsername);

        project.addDeveloper(developer);

        return developer;
    }

    private String generateGithubUrl(String githubUsername) {
        return "https://github.com/" + githubUsername;

    }

    private Developer findOrCreateDeveloper(String developerName, Project project) {
        Developer existingDeveloper = developerRepository.findByName(developerName);

        if (existingDeveloper != null) {
            return existingDeveloper;
        } else {
            Developer newDeveloper = new Developer();
            newDeveloper.setName(developerName);
            newDeveloper.setProject(project);

            developerRepository.save(newDeveloper);
            return newDeveloper;
        }
    }
}
