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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

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
    Logger logger = Logger.getLogger(SonarAnalysis.class.getName());
    private String sonarqubeUrl;

    public SonarAnalysis(Project project, String version, String sonarqubeUrl) throws IOException, InterruptedException {
        this.projectName = project.getName();
        this.projectOwner = project.getOwnerName();
        this.version = version;
        this.sonarqubeUrl = sonarqubeUrl;

        System.out.println("SonarAnalysis Version: " + version);

        createSonarFile();
        makeSonarAnalysis();
    }

    // Create Sonar Properties file
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
            writer.append("sonar.host.url=" + sonarqubeUrl + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    // Start Analysis with sonar scanner
    private void makeSonarAnalysis() throws IOException, InterruptedException {
        String projectDir = System.getProperty("user.dir");
        String reposDir = projectDir + File.separator + "repos";
        String sonnarScannerDir = projectDir + File.separator + "backend" + File.separator + "sonar-scanner";

        logger.info("projectDir: " + projectDir);
        logger.info("reposDir: " + reposDir);
        logger.info("sonnarScannerDir: " + sonnarScannerDir);

        if (DashboardApplication.isWindows()) {
            String batPath = sonnarScannerDir + File.separator + "sonar-scanner-5.0.1.3006-windows" + File.separator + "bin" + File.separator + "sonar-scanner.bat";
            logger.info("batPath: " + batPath);

            String command = "cmd /c cd " + reposDir + File.separator + projectName + " && " + batPath;
            logger.info("command: " + command);

            Process proc = Runtime.getRuntime().exec(command);

            logger.info("SonarQube analysis started for project: " + projectName);
            readProcessStreams(proc);
        }
        else {
            try {
                String bashPath = sonnarScannerDir + File.separator + "sonar-scanner-5.0.1.3006-linux" + File.separator + "bin" + File.separator + "sonar-scanner";
                logger.info("bashPath: " + bashPath);

                String command = "cd '" + reposDir + File.separator + projectName + "' ; " + bashPath;
                logger.info("command: " + command);

                ProcessBuilder pbuilder = new ProcessBuilder("bash", "-c", command);

                File err = new File("err.txt");
                pbuilder.redirectError(err);
                Process p = pbuilder.start();

                logger.info("SonarQube analysis started for project: " + projectName);
                readProcessStreams(p);
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

    private void readProcessStreams(Process process) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String inputLine;
        while ((inputLine = inputReader.readLine()) != null) {
            System.out.println(" " + inputLine);
        }
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            System.out.println(errorLine);
            // Show what path could not be found
            if (errorLine.contains("java.io.FileNotFoundException")) {
                System.out.println("File not found: " + errorLine.substring(errorLine.indexOf("java.io.FileNotFoundException")));
            }
        }
    }

    private void getMetricFromSonarQube() {
        try {
            URL url = new URL( sonarqubeUrl +"/api/measures/component?component=" + projectOwner + ":" + projectName+
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
            URL url = new URL(sonarqubeUrl +"/api/ce/component?component=" + projectOwner + ":" + projectName);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();
            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode: "+responsecode);

            else {
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
