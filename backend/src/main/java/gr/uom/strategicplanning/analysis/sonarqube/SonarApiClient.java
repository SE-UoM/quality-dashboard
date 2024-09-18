package gr.uom.strategicplanning.analysis.sonarqube;


import com.squareup.okhttp.Response;
import gr.uom.strategicplanning.analysis.HttpClient;
import gr.uom.strategicplanning.models.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for interacting with the SonarQube API to fetch project data and statistics.
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SonarApiClient extends HttpClient {
    private Response loginResponse;
    public String SONARQUBE_URL;

    public String LOGIN_USERNAME;
    public String LOGIN_PASSWORD;
    public String SONARQUBE_AUTH_URL;
    public String ISSUES_SEARCH_URL;
    public String LANGUAGES_URL;
    
    private int ARRAY_INDEX;
    private String EMPTY_PARAM;

    public SonarApiClient(String sonarUrl) {
        this.SONARQUBE_URL = sonarUrl;
        this.LOGIN_USERNAME = "admin";
        this.LOGIN_PASSWORD = "admin1";
        this.SONARQUBE_AUTH_URL = SONARQUBE_URL + "/api/authentication/login?login=" + LOGIN_USERNAME + "&password=" + LOGIN_PASSWORD;
        this.ISSUES_SEARCH_URL = SONARQUBE_URL + "/api/issues/search?componentKeys=";
        this.LANGUAGES_URL = SONARQUBE_URL + "/api/measures/component?metricKeys=ncloc_language_distribution&component=";
        this.ARRAY_INDEX = 0;
        this.EMPTY_PARAM = "";
    }

    /**
     * Some SonarQube projects have the component key as owner:projectName
     * This method tries to find the correct component key by searching by name
     * @return An Optional object containing the component key if found, or an empty Optional if not found
     */
    private Optional<JSONObject> findProjectComponentByName(Project project) throws IOException {
        String projectName = project.getName();
        String projectOwner = project.getOwnerName();

        String componentName = projectName;
        String searchByComponentKeyUrl = SONARQUBE_URL + "/api/components/show?component=" + componentName;

        System.out.println("In findProjectComponentByName");
        System.out.println("SONARQUBE_URL: " + SONARQUBE_URL);

        Response response = this.sendGetRequest(searchByComponentKeyUrl);

        // Check if we got a valid response and if not try to search by user:name
        if (!response.isSuccessful())
            componentName =  projectOwner + ":" + projectName;

        searchByComponentKeyUrl = SONARQUBE_URL + "/api/components/show?component=" + componentName;

        System.out.println("SONARQUBE URL: " + searchByComponentKeyUrl);

        response = this.sendGetRequest(searchByComponentKeyUrl);

        if (!response.isSuccessful())
            return Optional.empty();

        JSONObject jsonObject = this.convertResponseToJson(response);

        return Optional.of(jsonObject);
    }

    private String findComponentKey(Optional componentOptional) throws IOException {
        if (componentOptional.isEmpty())
            throw new IOException("Component optional is empty | File: SonarApiClient.java | Method: findComponentKey");

        JSONObject componentJSON = (JSONObject) componentOptional.get();
        JSONObject projectJSON = componentJSON.getJSONObject("component");

        return projectJSON.getString("key");
    }

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


        JSONObject issues = this.fetchIssues(project, EMPTY_PARAM);
        Double effortInMins = Double.valueOf(issues.get("effortTotal").toString());

        JSONObject codeSmellsJsonObject = fetchCodeSmells(project);
        int totalCodeSmells = codeSmellsJsonObject.getInt("total");

        commit.setTotalFiles(totalFiles);
        commit.setTotalLoC(totalLines);
        commit.setTechnicalDebt(effortInMins);
        commit.setTotalCodeSmells(totalCodeSmells);
        commit.setTechDebtPerLoC(effortInMins/ totalLines);
        commit.setTotalFiles(totalFiles);

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
        Optional<JSONObject> component = findProjectComponentByName(project);
        String componentKey = findComponentKey(component);
        String apiUrl = SONARQUBE_URL + "/api/measures/component?metricKeys=" + metricKey + "&component=" + componentKey;

        loginToSonar();

        Response response = this.sendGetRequest(apiUrl);
        JSONObject jsonObject = this.convertResponseToJson(response);

        JSONObject componentJSON = jsonObject.getJSONObject("component");
        JSONArray measures = componentJSON.getJSONArray("measures");

        if (measures.isEmpty()) return -1;

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
        Optional<JSONObject> component = findProjectComponentByName(project);
        String componentKey = findComponentKey(component);

        Response response = this.sendGetRequest(ISSUES_SEARCH_URL + componentKey + "&types=" + types);

        return this.convertResponseToJson(response);
    }

    /**
     * Fetches the language distribution (lines of code per language) from the SonarQube API for the given project.
     *
     * @param project The Project object for which the language distribution is fetched.
     * @return A collection of Language objects representing the language distribution.
     * @throws IOException If an I/O error occurs while communicating with the SonarQube API.
     */
    public Collection<ProjectLanguage> fetchLanguages(Project project) throws IOException {
        Collection<ProjectLanguage> languages = new ArrayList<>();

        Optional<JSONObject> projectComponent = findProjectComponentByName(project);
        String componentKey = findComponentKey(projectComponent);

        try {
            String requestURL = LANGUAGES_URL + componentKey;
            System.out.println("Request URL: " + requestURL);
            Response response = this.sendGetRequest(requestURL);

            JSONObject jsonObject = this.convertResponseToJson(response);
            JSONObject component = jsonObject.getJSONObject("component");
            JSONArray measures = component.getJSONArray("measures");

            String value = measures.getJSONObject(ARRAY_INDEX).get("value").toString();

            String regex = "(\\w+)=(\\d+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(value);

            while (matcher.find()) {
                String languageName = matcher.group(1);
                int loc = Integer.parseInt(matcher.group(2));

                ProjectLanguage language = new ProjectLanguage();
                language.setProject(project);
                language.setName(languageName);
                language.setLinesOfCode(loc);

                replaceKeyWithLangName(language);

                languages.add(language);
            }
        }
        catch (JSONException e) {
            System.out.println("No languages found for project " + project.getName());
        }

        return languages;
    }

    private void replaceKeyWithLangName(ProjectLanguage language) {
//        if (language.getName().equals("py")) language.setName("python");
//        if (language.getName().equals("cs")) language.setName("c#");
//        if (language.getName().equals("cxx")) language.setName("c++");

        // Call the languages/list API to get the full language name
        // If the language is not found, use the language name as is

        try {
            String apiUrl = SONARQUBE_URL + "/api/languages/list";
            Response response = this.sendGetRequest(apiUrl);
            JSONObject jsonObject = this.convertResponseToJson(response);
            JSONArray languages = jsonObject.getJSONArray("languages");

            for (int i = 0; i < languages.length(); i++) {
                JSONObject lang = languages.getJSONObject(i);
                String key = lang.getString("key");
                String name = lang.getString("name");

                if (language.getName().equals(key)) {
                    language.setName(name);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Logs in to SonarQube API using the provided credentials.
     *
     * @throws IOException If an I/O error occurs while logging in.
     */
    public void loginToSonar() throws IOException {
        loginResponse = this.sentPostAuthRequest(SONARQUBE_AUTH_URL);
    }

    public Collection<ProjectCodeSmellDistribution> fetchCodeSmellsDistribution(Project project) throws IOException {
        Optional<JSONObject> projectComponent = findProjectComponentByName(project);
        String componentKey = findComponentKey(projectComponent);

        String apiUrl = SONARQUBE_URL + "/api/issues/search?componentKeys=" + componentKey + "&types=CODE_SMELL&&facets=severities";

        Response response = this.sendGetRequest(apiUrl);
        JSONObject jsonObject = this.convertResponseToJson(response);

        JSONArray facets = jsonObject.getJSONArray("facets");
        JSONObject severities = facets.getJSONObject(ARRAY_INDEX);
        JSONArray values = severities.getJSONArray("values");

        int valuesSize = values.length();

        Collection<ProjectCodeSmellDistribution> codeSmellsDistribution = new ArrayList<>();
        for (int i = 0; i < valuesSize; i++) {
            JSONObject value = values.getJSONObject(i);
            String name = value.getString("val");
            int count = value.getInt("count");

            ProjectCodeSmellDistribution projectCodeSmellDistribution = new ProjectCodeSmellDistribution();
            projectCodeSmellDistribution.setProjectStats(project.getProjectStats());
            projectCodeSmellDistribution.setCodeSmell(name);
            projectCodeSmellDistribution.setCount(count);

            codeSmellsDistribution.add(projectCodeSmellDistribution);
        }

        return codeSmellsDistribution;
    }

    public int retrieveDataFromMeasures(Project project, String metric) throws IOException {
        this.loginToSonar();
        return this.fetchComponentMetrics(project, metric);
    }
}
