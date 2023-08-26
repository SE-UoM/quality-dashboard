package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CommitService {

    private CommitRepository commitRepository;
    private DeveloperService developerService;
    private CodeSmellService codeSmellService;
    private LanguageService languageService;
    private GithubApiClient githubApiClient;
    private final SonarApiClient sonarApiClient = new SonarApiClient();

    @Autowired
    public CommitService(DeveloperService developerService, CommitRepository commitRepository, LanguageService languageService, CodeSmellService codeSmellService
            , @Value("${github.token}") String githubToken) {
        this.developerService = developerService;
        this.commitRepository = commitRepository;
        this.codeSmellService = codeSmellService;
        this.languageService = languageService;
        this.githubApiClient = new GithubApiClient(githubToken);
    }

    public void populateCommit(Commit commit, Project project) throws IOException {
        commit.setCommitDate(githubApiClient.fetchCommitDate(project, commit));
        commit.setDeveloper(developerService.populateDeveloperData(project, commit));
        commit.setCodeSmells(codeSmellService.populateCodeSmells(project, commit));
        commit.setTotalCodeSmells(commit.getCodeSmells().size());
        commit.setProject(project);
        sonarApiClient.fetchCommitData(project, commit);

        saveCommit(commit);
    }


    private void saveCommit(Commit commit) {
        commitRepository.save(commit);
    }
}
