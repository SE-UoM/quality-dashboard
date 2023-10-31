package gr.uom.strategicplanning.analysis.sonarqube;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.sun.istack.NotNull;
import gr.uom.strategicplanning.DashboardApplication;
import gr.uom.strategicplanning.models.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SonarAnalysis {

    String projectName;
    String projectOwner;
    String sha;
    Integer TD;
    Integer Complexity;
    Integer LOC;
    String version;

    public SonarAnalysis(Project project, String version) throws IOException, InterruptedException {
        this.projectName = project.getName();
        this.projectOwner = project.getOwnerName();
        this.version = version;

        //create file
        createSonarFile();
        //start analysis
        makeSonarAnalysis();
        //Get TD from SonarQube
//        getMetricFromSonarQube();
    }


    //Create Sonar Properties file
    private void createSonarFile() throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir")+"/repos/" + projectName + "/sonar-project.properties")));
            writer.write("sonar.projectKey=" + projectOwner +":"+ projectName + System.lineSeparator());
            writer.append("sonar.projectName=" + projectOwner +":"+ projectName + System.lineSeparator());
            writer.append("sonar.projectVersion=" + version + System.lineSeparator());
            writer.append("sonar.sourceEncoding=UTF-8" + System.lineSeparator());
            writer.append("sonar.sources=." + System.lineSeparator());
            writer.append("sonar.java.binaries=." + System.lineSeparator());
            writer.append("sonar.host.url=" + SonarApiClient.SONARQUBE_URL + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    //Start Analysis with sonar scanner
    private void makeSonarAnalysis() throws IOException, InterruptedException {
        if (DashboardApplication.isWindows()) {
            Process proc = Runtime.getRuntime().exec("cmd /c cd " +System.getProperty("user.dir")+ "\\repos\\" + projectName +
                    " && ..\\sonar-scanner-5.0.1.3006-windows\\bin\\sonar-scanner.bat");
            System.out.println("start analysis");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                System.out.println(" "+inputLine);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println(errorLine);
            }
        }
        else {
            try {
                ProcessBuilder pbuilder = new ProcessBuilder("bash", "-c",
                        "cd '" + System.getProperty("user.dir") +"/repos/"+ projectName+"' ; ../sonar-scanner-4.7.0.2747-linux/bin/sonar-scanner");
                File err = new File("err.txt");
                pbuilder.redirectError(err);
                Process p = pbuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(" "+line);
                }
                BufferedReader reader_2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line_2;
                while ((line_2 = reader_2.readLine()) != null) {
                    System.out.println(line_2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //wait till sonarqube finishes analysing
        while (!isFinishedAnalyzing()) {
            Thread.sleep(1000);
        }
        Thread.sleep(500);
    }

    private void getMetricFromSonarQube() {
        try {
            URL url = new URL( SonarApiClient.SONARQUBE_URL +"/api/measures/component?component=" + projectOwner + ":" + projectName+
                    "&metricKeys=sqale_index,complexity,ncloc");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200)
                System.err.println(responsecode);
            else{
                Scanner sc = new Scanner(url.openStream());
                String inline="";
                while(sc.hasNext()){
                    inline+=sc.nextLine();
                }
                sc.close();

                JSONParser parse = new JSONParser();
                JSONObject jobj = (JSONObject)parse.parse(inline);
                JSONObject jobj1= (JSONObject) jobj.get("component");
                JSONArray jsonarr_1 = (JSONArray) jobj1.get("measures");

                for(int i=0; i<jsonarr_1.size(); i++){
                    JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
                    if(jsonobj_1.get("metric").toString().equals("sqale_index"))
                        TD= Integer.parseInt(jsonobj_1.get("value").toString());
                    if(jsonobj_1.get("metric").toString().equals("complexity"))
                        Complexity= Integer.parseInt(jsonobj_1.get("value").toString());
                    if(jsonobj_1.get("metric").toString().equals("ncloc"))
                        LOC= Integer.parseInt(jsonobj_1.get("value").toString());
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /*
     * Returns if the project is finished being analyzed
     */
    public boolean isFinishedAnalyzing(){
        boolean finished=false;
        try {
            URL url = new URL(SonarApiClient.SONARQUBE_URL +"/api/ce/component?component=" + projectOwner + ":" + projectName);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode: "+responsecode);
            else{
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNext()){
                    String line=sc.nextLine();
                    if(line.trim().contains("\"analysisId\":") &&
                            line.trim().contains("\"queue\":[],")){
                        finished=true;
                    }
                }
                sc.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return finished;
    }

}
