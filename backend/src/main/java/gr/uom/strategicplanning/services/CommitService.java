package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CommitService {
    @Value("${github.token}")
    private String githubToken;
    private CommitRepository commitRepository;
    private DeveloperService developerService;
    private CodeSmellService codeSmellService;
    private GithubApiClient githubApiClient = new GithubApiClient(githubToken);

    @Autowired
    public CommitService(DeveloperService developerService, CommitRepository commitRepository, CodeSmellService codeSmellService) {
        this.developerService = developerService;
        this.commitRepository = commitRepository;
        this.codeSmellService = codeSmellService;
    }

    public void populateCommit(Commit commit, Project project) throws IOException {
        commit.setCommitDate(githubApiClient.fetchCommitDate(project, commit));
        commit.setDeveloper(developerService.populateDeveloperData(project, commit));
        commit.setCodeSmells(codeSmellService.populateCodeSmells(project, commit));
        // commitRepository.save(commit);
    }
}
