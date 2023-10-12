package gr.uom.strategicplanning.controllers;

import gr.uom.strategicplanning.controllers.dtos.GeneralStatsDTO;
import gr.uom.strategicplanning.controllers.responses.implementations.DeveloperResponse;
import gr.uom.strategicplanning.controllers.responses.implementations.LanguageResponse;
import gr.uom.strategicplanning.models.domain.OrganizationCodeSmellDistribution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/examples")
public class ExamplesController {

    @GetMapping("/projects-info")
    public ResponseEntity getProjectsInfo() {
        Collection projectsInfo = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String projectName = "Project " + i;
            String projectOwner = "Owner " + i;
            int projectCommits = (int) Math.random();
            int getProjectStars = (int) Math.random();
            int projectForks = (int) Math.random();

            Map<String, Object> projectInfoMap = new HashMap<>();
            projectInfoMap.put("name", projectName);
            projectInfoMap.put("owner", projectOwner);
            projectInfoMap.put("totalContributions", projectCommits);
            projectInfoMap.put("stars", getProjectStars);
            projectInfoMap.put("forks", projectForks);

            projectsInfo.add(projectInfoMap);
        }

        return ResponseEntity.ok(projectsInfo);
    }

    @GetMapping("/general-stats")
    public ResponseEntity getGeneralStats() {
        GeneralStatsDTO generalStats = new GeneralStatsDTO();
        generalStats.setTotalProjects(125);
        generalStats.setTotalLanguages(20);
        generalStats.setTotalCommits(10000);
        generalStats.setTotalFiles(5467);
        generalStats.setTotalLinesOfCode(184367);
        generalStats.setTotalDevs(80);

        return ResponseEntity.ok(generalStats);
    }

    @GetMapping("/project-names")
    public ResponseEntity getProjectNames() {

        Collection projectsInfoResponse = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> projectInfoMap = new HashMap<>();
            projectInfoMap.put("name", "Project " + i);

            projectsInfoResponse.add(projectInfoMap);
        }
        return ResponseEntity.ok(projectsInfoResponse);
    }

    @GetMapping("/top-developers")
    public ResponseEntity getTopDevs() {
        DeveloperResponse dev1 = new DeveloperResponse();
        dev1.setId(1L);
        dev1.setName("Dev 1");
        dev1.setGithubUrl("https://avatars.githubusercontent.com/u/1?v=4");
        dev1.setTotalCommits(100);
        dev1.setTotalCodeSmells(10);
        dev1.setCodeSmellsPerCommit(0.1);

        DeveloperResponse dev2 = new DeveloperResponse();
        dev2.setId(2L);
        dev2.setName("Dev 2");
        dev2.setGithubUrl("https://avatars.githubusercontent.com/u/2?v=4");
        dev2.setTotalCommits(200);
        dev2.setTotalCodeSmells(20);
        dev2.setCodeSmellsPerCommit(0.2);

        DeveloperResponse dev3 = new DeveloperResponse();
        dev3.setId(3L);
        dev3.setName("Dev 3");
        dev3.setGithubUrl("https://avatars.githubusercontent.com/u/3?v=4");
        dev3.setTotalCommits(300);
        dev3.setTotalCodeSmells(30);
        dev3.setCodeSmellsPerCommit(0.3);

        DeveloperResponse dev4 = new DeveloperResponse();
        dev4.setId(4L);
        dev4.setName("Dev 4");
        dev4.setGithubUrl("https://avatars.githubusercontent.com/u/4?v=4");
        dev4.setTotalCommits(400);
        dev4.setTotalCodeSmells(40);
        dev4.setCodeSmellsPerCommit(0.4);

        DeveloperResponse dev5 = new DeveloperResponse();
        dev5.setId(5L);
        dev5.setName("Dev 5");
        dev5.setGithubUrl("https://avatars.githubusercontent.com/u/5?v=4");
        dev5.setTotalCommits(500);
        dev5.setTotalCodeSmells(50);
        dev5.setCodeSmellsPerCommit(0.5);

        DeveloperResponse dev6 = new DeveloperResponse();
        dev6.setId(6L);
        dev6.setName("Dev 6");
        dev6.setGithubUrl("https://avatars.githubusercontent.com/u/6?v=4");
        dev6.setTotalCommits(600);
        dev6.setTotalCodeSmells(60);
        dev6.setCodeSmellsPerCommit(0.6);

        DeveloperResponse dev7 = new DeveloperResponse();
        dev7.setId(7L);
        dev7.setName("Dev 7");
        dev7.setGithubUrl("https://avatars.githubusercontent.com/u/7?v=4");
        dev7.setTotalCommits(700);
        dev7.setTotalCodeSmells(70);
        dev7.setCodeSmellsPerCommit(0.7);

        DeveloperResponse dev8 = new DeveloperResponse();
        dev8.setId(8L);
        dev8.setName("Dev 8");
        dev8.setGithubUrl("https://avatars.githubusercontent.com/u/8?v=4");
        dev8.setTotalCommits(800);
        dev8.setTotalCodeSmells(80);
        dev8.setCodeSmellsPerCommit(0.8);

        DeveloperResponse dev9 = new DeveloperResponse();
        dev9.setId(9L);
        dev9.setName("Dev 9");
        dev9.setGithubUrl("https://avatars.githubusercontent.com/u/9?v=4");
        dev9.setTotalCommits(900);
        dev9.setTotalCodeSmells(90);
        dev9.setCodeSmellsPerCommit(0.9);

        DeveloperResponse dev10 = new DeveloperResponse();
        dev10.setId(10L);
        dev10.setName("Dev 10");
        dev10.setGithubUrl("https://avatars.githubusercontent.com/u/10?v=4");
        dev10.setTotalCommits(1000);
        dev10.setTotalCodeSmells(100);
        dev10.setCodeSmellsPerCommit(1.0);

        Collection<DeveloperResponse> topDevs = new ArrayList<>();
        topDevs.add(dev1);
        topDevs.add(dev2);
        topDevs.add(dev3);
        topDevs.add(dev4);
        topDevs.add(dev5);
        topDevs.add(dev6);
        topDevs.add(dev7);
        topDevs.add(dev8);
        topDevs.add(dev9);
        topDevs.add(dev10);

        return ResponseEntity.ok(topDevs);
    }

    @GetMapping("/language-names")
    public ResponseEntity getLangNames() {
        Collection languages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> langMap = new HashMap<>();
            langMap.put("name", "Language " + i);
            languages.add(langMap);
        }

        return ResponseEntity.ok(languages);
    }

    @GetMapping("/top-projects")
    public ResponseEntity getTopProjects() {
        Collection projects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("name", "Project " + i);
            projectMap.put("totalCommits", 1000 + i);
            projectMap.put("totalCodeSmells", 100 + i);
            projectMap.put("codeSmellsPerCommit", 0.1 + i);
            projects.add(projectMap);
        }

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/developers-info")
    public ResponseEntity getDevsInfo() {
        Collection response = new ArrayList();

        for (int i = 0; i < 10; i++) {
            Map<String, Object> devMap = new HashMap<>();
            devMap.put("name", "Dev " + i);
            devMap.put("avatarUrl", "https://avatars.githubusercontent.com/u/1?v=4");

            response.add(devMap);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/most-active-developer")
    public ResponseEntity getMostActiveDev() {
        Map<String, Object> mostActiveDeveloperResponse = new HashMap<>();
        mostActiveDeveloperResponse.put("name", "Dev 1");
        mostActiveDeveloperResponse.put("avatarUrl", "https://avatars.githubusercontent.com/u/1?v=4");
        mostActiveDeveloperResponse.put("totalCommits", 1000);
        mostActiveDeveloperResponse.put("totalIssues", 100);
        mostActiveDeveloperResponse.put("issuesPerContribution", 0.1);

        return ResponseEntity.ok(mostActiveDeveloperResponse);
    }

    @GetMapping("/most-active-project")
    public ResponseEntity getMostActiveProject() {
        Map<String, Object> simpleProjectResponse = new HashMap<>();
        simpleProjectResponse.put("name", "Project 1");
        simpleProjectResponse.put("owner", "Owner 1");
        simpleProjectResponse.put("stars", 120);
        simpleProjectResponse.put("files", 100);
        simpleProjectResponse.put("loc", 10000);
        simpleProjectResponse.put("techDebt", 1000);
        simpleProjectResponse.put("totalCodeSmells", 100);
        simpleProjectResponse.put("totalCommits", 1000);
        simpleProjectResponse.put("totalForks", 100);

        return ResponseEntity.ok(simpleProjectResponse);
    }

    @GetMapping("/most-starred-project")
    public ResponseEntity getMostStarredProject() {
        Map<String, Object> simpleProjectResponse = new HashMap<>();
        simpleProjectResponse.put("name", "Project 1");
        simpleProjectResponse.put("owner", "Owner 1");
        simpleProjectResponse.put("stars", 50);
        simpleProjectResponse.put("files", 100);
        simpleProjectResponse.put("loc", 1223);
        simpleProjectResponse.put("techDebt", 123);
        simpleProjectResponse.put("totalCodeSmells", 100);
        simpleProjectResponse.put("totalCommits", 123);
        simpleProjectResponse.put("totalForks", 3);

        return ResponseEntity.ok(simpleProjectResponse);
    }

    @GetMapping("/most-forked-project")
    public ResponseEntity getMostForkedProject() {
        Map<String, Object> simpleProjectResponse = new HashMap<>();
        simpleProjectResponse.put("name", "Project 1");
        simpleProjectResponse.put("owner", "Owner 1");
        simpleProjectResponse.put("stars", 50);
        simpleProjectResponse.put("files", 100);
        simpleProjectResponse.put("loc", 1223);
        simpleProjectResponse.put("techDebt", 123);
        simpleProjectResponse.put("totalCodeSmells", 100);
        simpleProjectResponse.put("totalCommits", 123);
        simpleProjectResponse.put("totalForks", 3);

        return ResponseEntity.ok(simpleProjectResponse);
    }

    @GetMapping("/last-analysis-date")
    public ResponseEntity getLastAnalysisDate() {
        Map<String, Object> lastAnalysisDateResponse = new HashMap<>();
        lastAnalysisDateResponse.put("lastAnalysisDate", "2021/05/01");

        return ResponseEntity.ok(lastAnalysisDateResponse);
    }

    @GetMapping("/top-languages")
    public ResponseEntity getTopLangs() {
        LanguageResponse lang1 = new LanguageResponse();
        lang1.setId(1L);
        lang1.setName("JAVA");
        lang1.setLinesOfCode(1000);

        LanguageResponse lang2 = new LanguageResponse();
        lang2.setName("Language 2");
        lang2.setId(2L);
        lang2.setName("C#");
        lang2.setLinesOfCode(800);

        LanguageResponse lang3 = new LanguageResponse();
        lang3.setId(3L);
        lang3.setName("C++");
        lang3.setLinesOfCode(500);

        Map<String, LanguageResponse> topLanguagesResponse = new HashMap<>();
        topLanguagesResponse.put("1", lang1);
        topLanguagesResponse.put("2", lang2);
        topLanguagesResponse.put("3", lang3);

        return ResponseEntity.ok(topLanguagesResponse);
    }

    @GetMapping("/total-tech-debt")
    public ResponseEntity getTotalTechDebt() {
        Map<String, Object> totalTechDebtResponse = new HashMap<>();
        totalTechDebtResponse.put("totalTechDebtHours", 59);
        totalTechDebtResponse.put("totalTechDebtCost", 1000);
        totalTechDebtResponse.put("techDebtCostPerMonth", 100);
        totalTechDebtResponse.put("tdInMinutes", 1000);

        return ResponseEntity.ok(totalTechDebtResponse);
    }

    @GetMapping("/tech-debt-statistics")
    public ResponseEntity getTdStats() {
        Map<String, Object> techDebtStatsResponse = new HashMap<>();
        techDebtStatsResponse.put("minProjectTechDebt", 100);
        techDebtStatsResponse.put("maxProjectTechDebt", 1000);
        techDebtStatsResponse.put("avgTechDebt", 500);
        techDebtStatsResponse.put("avgTechDebtPerLOC", 0.5);

        return ResponseEntity.ok(techDebtStatsResponse);
    }

    @GetMapping("/language-distribution")
    public ResponseEntity getLanguageDistribution() {
        Collection<LanguageResponse> langs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            LanguageResponse lang = new LanguageResponse();
            lang.setId((long) i);
            lang.setName("Language " + i);
            lang.setLinesOfCode(10 + i);

            langs.add(lang);
        }

        int totalLanguages = langs.size();

        Map<String, Object> languageDistributionResponse = new HashMap<>();
        languageDistributionResponse.put("totalLanguages", totalLanguages);
        languageDistributionResponse.put("languageDistribution", langs);

        return ResponseEntity.ok(languageDistributionResponse);
    }

    @GetMapping("/code-smells-distribution")
    public ResponseEntity getCodeSmellsDistribution() {
        Collection<OrganizationCodeSmellDistribution> codeSmells = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            OrganizationCodeSmellDistribution codeSmell = new OrganizationCodeSmellDistribution();
            codeSmell.setId((long) i);
            codeSmell.setSeverity("Minor");
            codeSmell.setCount(10 + i);

            codeSmells.add(codeSmell);
        }

        int totalCodeSmells = codeSmells.size();

        Map<String, Object> codeSmellsDistributionResponse = new HashMap<>();
        codeSmellsDistributionResponse.put("totalCodeSmells", totalCodeSmells);
        codeSmellsDistributionResponse.put("codeSmellsDistribution", codeSmells);

        return ResponseEntity.ok(codeSmellsDistributionResponse);
    }

    @GetMapping("/all-commits")
    public ResponseEntity getAllCommits() {
        Collection commits = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Map commitResponse = new HashMap<>();
            commitResponse.put("sha", "sha" + i);

            // Random date between 5/9/2022 and 5/9/2023
            long minDay = LocalDate.of(2022, 5, 9).toEpochDay();
            long maxDay = LocalDate.of(2023, 5, 9).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);

            commitResponse.put("date", randomDay);

            commits.add(commitResponse);
        }

        return ResponseEntity.ok(commits);
    }
}
