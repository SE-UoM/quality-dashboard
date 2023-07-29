package gr.uom.strategicplanning.analysis.sonarqube;


import com.squareup.okhttp.Response;
import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.models.Commit;
import gr.uom.strategicplanning.models.Language;
import gr.uom.strategicplanning.models.Project;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SonarApiClient extends HttpClient {
    private Response loginResponse;
    public static final String SONARQUBE_URL = "http://localhost:9000";
    public static final String LOGIN_USERNAME = "admin";
    public static final String LOGIN_PASSWORD = "admin1";

    public static final String SONARQUBE_AUTH_URL = SONARQUBE_URL + "/api/authentication/login" +
            "?login=" + LOGIN_USERNAME + "&password=" + LOGIN_PASSWORD;

    public static final String ISSUES_SEARCH_URL = SONARQUBE_URL + "/api/issues/search?ps=500&componentKeys=";
    public static final String LANGUAGES_URL = SONARQUBE_URL + "/api/measures/component?metricKeys=ncloc_language_distribution&component=";
    
    private final int ARRAY_INDEX = 0;
    private final String EMPTY_PARAM = "";

    public void fetchProjectData(Project project) throws IOException {
        this.loginToSonar();

        // Fetch the data from component metrics
        Integer totalFiles = this.fetchComponentMetrics(project, "files");
        Integer totalLines = this.fetchComponentMetrics(project, "ncloc");

        Collection<Language> languageDistribution = this.fetchLanguages(project);

        JSONObject issues = this.fetchIssues(project, EMPTY_PARAM);
        Number effortInMins = (Number) issues.get("effortTotal");

        JSONObject codeSmellsJsonObject = this.fetchIssues(project, "CODE_SMELL");
        Integer totalCodeSmells = codeSmellsJsonObject.getInt("total");


        // Update the project
        Commit commit = new Commit();
        commit.setTotalCodeSmells(totalCodeSmells);
        commit.setTotalFiles(totalFiles);
        commit.setTechnicalDebt(effortInMins);
        commit.setTotalLoC(totalLines);
        commit.calculateTechDebtPerLoC();
        commit.setLanguages(languageDistribution);
        commit.setProject(project);

        project.addCommit(commit);

        System.out.println("Project: " + System.lineSeparator() + project);
    }

    private Integer fetchComponentMetrics(Project project, String metricKey) throws IOException {
        String apiUrl = SONARQUBE_URL + "/api/measures/component?metricKeys=" + metricKey + "&component=" + project.getName();

        // Send the GET request
        Response response = this.sendGetRequest(apiUrl);

        JSONObject jsonObject = this.convertResponseToJson(response);

        // Get the measures array
        JSONObject component = jsonObject.getJSONObject("component");
        JSONArray measures = component.getJSONArray("measures");

        // Get the metrics map
        Map metrics_map = measures.getJSONObject(ARRAY_INDEX).toMap();
        String value = (String) metrics_map.get("value");

        return Integer.parseInt(value);
    }

    private JSONObject fetchIssues(Project project, String types) throws IOException {
        // Send the GET request
        Response response = this.sendGetRequest(ISSUES_SEARCH_URL + project.getName() + "&types=" + types);

        return this.convertResponseToJson(response);
    }

    private Collection<Language> fetchLanguages(Project project) throws IOException {
        // Send the GET request
        Response response = this.sendGetRequest(LANGUAGES_URL + project.getName());

        JSONObject jsonObject = this.convertResponseToJson(response);
        JSONObject component = jsonObject.getJSONObject("component");
        JSONArray measures = component.getJSONArray("measures");
        String value = measures.getJSONObject(ARRAY_INDEX).get("value").toString();

        // Regular expression to match language-value pairs
        String regex = "(\\w+)=(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        // Create a list to store the Language objects
        ArrayList<Language> languages = new ArrayList<>();

        // Find all matches using the regex and extract language names and values
        while (matcher.find()) {
            String languageName = matcher.group(1);
            int numericValue = Integer.parseInt(matcher.group(2));

            Language language = new Language();
            language.setName(languageName);
            language.setLinesOfCode(numericValue);

            languages.add(language);
        }

        return languages;
    }

    public void loginToSonar() throws IOException {
        loginResponse = this.sentPostAuthRequest(SONARQUBE_AUTH_URL);
    }

    public static void main(String[] args) throws IOException {
        Project project = new Project();
        project.setRepoUrl("https://github.com/GeorgeApos/code_metadata_extractor");
        project.setName("test");

        // Fetch the analysis results.
        System.getLogger("SonarAnalyzer").log(System.Logger.Level.INFO, "Fetching analysis data from SonarQube API client for project: " + project.getName());

        SonarApiClient sonarApiClient = new SonarApiClient();
        sonarApiClient.fetchProjectData(project);

        System.getLogger("SonarAnalyzer").log(System.Logger.Level.INFO, "Analysis data fetched from SonarQube API client for project: " + project.getName());
    }
}
