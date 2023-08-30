package gr.uom.strategicplanning.analysis.sonarqube;


import com.squareup.okhttp.Response;
import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.analysis.github.GithubApiClient;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Language;
import gr.uom.strategicplanning.models.domain.LanguageStats;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.stats.ProjectStats;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for interacting with the SonarQube API to fetch project data and statistics.
 */
public class SonarApiClient extends HttpClient {
    private Response loginResponse;
    public static final String SONARQUBE_URL = "http://195.251.210.147:9952";
    public static final String LOGIN_USERNAME = "admin";
    public static final String LOGIN_PASSWORD = "admin1";

    public final String SONARQUBE_AUTH_URL = SONARQUBE_URL + "/api/authentication/login" +
            "?login=" + LOGIN_USERNAME + "&password=" + LOGIN_PASSWORD;

    public final String ISSUES_SEARCH_URL = SONARQUBE_URL + "/api/issues/search?ps=500&componentKeys=";
    public final String LANGUAGES_URL = SONARQUBE_URL + "/api/measures/component?metricKeys=ncloc_language_distribution&component=";
    
    private final int ARRAY_INDEX = 0;
    private final String EMPTY_PARAM = "";

    /**
     * Fetches project data and statistics from SonarQube API and updates the provided project object with the results.
     *
     * @param project The Project object to which the fetched data and statistics will be added.
     * @throws IOException If an I/O error occurs while communicating with the SonarQube API.
     */
    public void fetchCommitData(Project project, Commit commit) throws IOException, InterruptedException {
        this.loginToSonar();

        int totalFiles = this.fetchComponentMetrics(project, "files");
        int totalLines = this.fetchComponentMetrics(project, "ncloc");

//        Collection<LanguageStats> languageDistribution = this.fetchLanguages(project);

        JSONObject issues = this.fetchIssues(project, EMPTY_PARAM);
        Double effortInMins = Double.valueOf(issues.get("effortTotal").toString());

        JSONObject codeSmellsJsonObject = fetchCodeSmells(project);
        int totalCodeSmells = codeSmellsJsonObject.getInt("total");

        commit.setTotalFiles(totalFiles);
        commit.setTotalLoC(totalLines);
//        commit.setLanguages(languageDistribution);
        commit.setTechnicalDebt(effortInMins);
        commit.setTotalCodeSmells(totalCodeSmells);
        commit.setTechDebtPerLoC(effortInMins/ totalLines);
        commit.setTotalFiles(totalFiles);
//        commit.setTotalLanguages(languageDistribution.size());

    }

    public JSONObject fetchCodeSmells(Project project) throws IOException {
        return this.fetchIssues(project, "CODE_SMELL");
    }

    /**
     * Fetches a specific component metric for the given project from the SonarQube API.
     * See http://localhost:9000/api/metrics/search for a list of all available metrics.
     *
     * @param project   The Project object for which the metric is fetched.
     * @param metricKey The key of the metric to be fetched.
     * @return The value of the requested metric as an Integer.
     * @throws IOException If an I/O error occurs while communicating with the SonarQube API.
     */
    private int fetchComponentMetrics(Project project, String metricKey) throws IOException {
        String apiUrl = SONARQUBE_URL + "/api/measures/component?metricKeys=" + metricKey + "&component=" + project.getName();

        loginToSonar();

        Response response = this.sendGetRequest(apiUrl);

        JSONObject jsonObject = this.convertResponseToJson(response);

        JSONObject component = jsonObject.getJSONObject("component");
        JSONArray measures = component.getJSONArray("measures");

        if (measures.length() == 0) {
            return -1;
        }

        Map metrics_map = measures.getJSONObject(ARRAY_INDEX).toMap();
        String value = (String) metrics_map.get("value");

        return Integer.parseInt(value);
    }


    /**
     * Fetches issues (e.g., bugs, code smells, vulnerabilities) from the SonarQube API for the given project and issue types.
     *
     * @param project The Project object for which the issues are fetched.
     * @param types   The types of issues to be fetched (e.g., "BUG", "VULNERABILITY", "CODE_SMELL"). Use an empty string for all issue types.
     * @return A JSONObject representing the fetched issues.
     * @throws IOException If an I/O error occurs while communicating with the SonarQube API.
     */
    private JSONObject fetchIssues(Project project, String types) throws IOException {
        // Send the GET request
        Response response = this.sendGetRequest(ISSUES_SEARCH_URL + project.getName() + "&types=" + types);

        return this.convertResponseToJson(response);
    }

    /**
     * Fetches the language distribution (lines of code per language) from the SonarQube API for the given project.
     *
     * @param project The Project object for which the language distribution is fetched.
     * @return A collection of Language objects representing the language distribution.
     * @throws IOException If an I/O error occurs while communicating with the SonarQube API.
     */
    private Collection<LanguageStats> fetchLanguages(Project project) throws IOException {
        Response response = this.sendGetRequest(LANGUAGES_URL + project.getName());

        JSONObject jsonObject = this.convertResponseToJson(response);
        JSONObject component = jsonObject.getJSONObject("component");
        JSONArray measures = component.getJSONArray("measures");

        String missingValue = "?";
        if (measures.length() == 0) {
            missingValue = "";
        }

        String value = measures.getJSONObject(ARRAY_INDEX).get("value").toString();

        String regex = "(\\w+)=(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        ArrayList<LanguageStats> languages = new ArrayList<>();

        while (matcher.find()) {
            String languageName = matcher.group(1);
            int loc = Integer.parseInt(matcher.group(2));

            Language language = new Language();
            language.setName(languageName);
            LanguageStats languageStats = new LanguageStats();
            languageStats.setProject(project);
            languageStats.setLanguage(language);
            languageStats.setLinesOfCode(loc);
            languages.add(languageStats);
        }

        return languages;
    }


    /**
     * Logs in to SonarQube API using the provided credentials.
     *
     * @throws IOException If an I/O error occurs while logging in.
     */
    public void loginToSonar() throws IOException {
        loginResponse = this.sentPostAuthRequest(SONARQUBE_AUTH_URL);
    }

}
