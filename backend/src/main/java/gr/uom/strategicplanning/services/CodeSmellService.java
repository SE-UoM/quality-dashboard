package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.sonarqube.SonarApiClient;
import gr.uom.strategicplanning.models.domain.CodeSmell;
import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import org.aspectj.apache.bcel.classfile.Code;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CodeSmellService {

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
            int line = issue.getInt("line");
            String severity = issue.getString("severity");
            String message = issue.getString("message"); // Extract message
            String effortOrDebt = issue.getString("effort"); // Extract effort or debt

            CodeSmell codeSmell = new CodeSmell();
            codeSmell.setName(componentName);
            codeSmell.setLine(line);
            codeSmell.setRemediationTime(Integer.parseInt(effortOrDebt));
            codeSmell.setSeverityLevel(severity);
            codeSmell.setName(message);
            codeSmellList.add(codeSmell);
        }

        return codeSmellList;
    }
}
