package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.github.GitClient;
import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.domain.CodeSmell;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Developer;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

@Service
public class CommitService {

    private CommitRepository commitRepository;
    private DeveloperService developerService;
    private CodeSmellService codeSmellService;
    private LanguageService languageService;
    private GithubApiClient githubApiClient;
    private final SonarApiClient sonarApiClient;

    @Autowired
    public CommitService(
            DeveloperService developerService,
            CommitRepository commitRepository,
            LanguageService languageService,
            CodeSmellService codeSmellService,
            @Value("${github.token}") String githubToken,
            @Value("${sonar.sonarqube.url}") String sonarApiUrl
    ) {
        this.developerService = developerService;
        this.commitRepository = commitRepository;
        this.codeSmellService = codeSmellService;
        this.languageService = languageService;
        this.githubApiClient = new GithubApiClient(githubToken);
        this.sonarApiClient = new SonarApiClient(sonarApiUrl);
    }

    public void populateCommit(Commit commit, Project project) throws IOException, InterruptedException {
        Date commitDate = GitClient.fetchCommitDate(project, commit);
        commit.setCommitDate(commitDate);

        Developer developer = developerService.populateDeveloperData(project, commit);
        commit.setDeveloper(developer);

        Collection<CodeSmell> codeSmells = codeSmellService.populateCodeSmells(project, commit);
        commit.setCodeSmells(codeSmells);

        int totalCodeSmells = commit.getCodeSmells().size();
        commit.setTotalCodeSmells(totalCodeSmells);

        commit.setProject(project);

        sonarApiClient.fetchCommitData(project, commit);

        saveCommit(commit);
    }


    private void saveCommit(Commit commit) {
        commitRepository.save(commit);
    }
}
