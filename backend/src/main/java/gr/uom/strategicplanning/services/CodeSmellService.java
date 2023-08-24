package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.domain.CodeSmell;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.repositories.CodeSmellRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CodeSmellService {

    @Autowired
    private CodeSmellRepository codeSmellRepository;
    private final SonarApiClient sonarApiClient = new SonarApiClient();

    public Collection<CodeSmell> populateCodeSmells(Project project, Commit commit) throws IOException {
        JSONObject codeSmells = sonarApiClient.fetchCodeSmells(project);
        return codeSmellsToCodeSmellList(codeSmells, commit);
    }

    public Collection<CodeSmell> codeSmellsToCodeSmellList(JSONObject codeSmells, Commit commit) {
        JSONArray issuesArray = codeSmells.getJSONArray("issues");
        List<CodeSmell> codeSmellList = new ArrayList<>();

        for (int i = 0; i < issuesArray.length(); i++) {
            JSONObject issue = issuesArray.getJSONObject(i);
            String componentName = issue.getString("component");

            // Extract line info
            JSONObject lineInfo = issue.getJSONObject("textRange");
            int line = lineInfo.getInt("startLine");

            String severity = issue.getString("severity");
            String message = issue.getString("message");
            String effortOrDebt = issue.getString("debt");

            Number effort = extractNumericalPart(effortOrDebt);

            CodeSmell codeSmell = new CodeSmell();
            codeSmell.setName(componentName);
            codeSmell.setLine(line);
            codeSmell.setRemediationTime((Integer) effort);
            codeSmell.setSeverityLevel(severity);
            codeSmell.setName(message);
            codeSmellList.add(codeSmell);

            saveCodeSmell(codeSmell);
        }

        return codeSmellList;
    }

    private void saveCodeSmell(CodeSmell codeSmell) {
        codeSmellRepository.save(codeSmell);
    }

    private Number extractNumericalPart(String effortOrDebt) {
        Pattern pattern = Pattern.compile("(\\d+)min");
        Matcher matcher = pattern.matcher(effortOrDebt);

        if (!matcher.find())
            throw new IllegalArgumentException("Could not parse effort or debt string: " + effortOrDebt);

        String numberBeforeMinStr = matcher.group(1);
        Number numberBeforeMin = Integer.parseInt(numberBeforeMinStr);

        return numberBeforeMin;
    }
}
