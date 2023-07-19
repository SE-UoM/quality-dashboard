package gr.uom.strategicplanning.analysis.sonarqube;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SonarApiClient {

    public void startSonarAnalysis(String projectOwner, String projectName) throws IOException, InterruptedException {
        createSonarFile(projectOwner, projectName);
        makeSonarAnalysis(projectOwner, projectName);
        getMetricsFromSonarQube(projectOwner, projectName);
    }

    private void createSonarFile(String projectOwner, String projectName) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir")+"/" + projectName + "/sonar-project.properties")));
            writer.write("sonar.projectKey=" + projectOwner +":"+ projectName + System.lineSeparator());
            writer.append("sonar.projectName=" + projectOwner +":"+ projectName + System.lineSeparator());
            writer.append("sonar.sourceEncoding=UTF-8" + System.lineSeparator());
            writer.append("sonar.sources=." + System.lineSeparator());
            writer.append("sonar.java.binaries=." + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    private void makeSonarAnalysis(String projectOwner, String projectName) throws IOException, InterruptedException {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            Process proc = Runtime.getRuntime().exec("cmd /c cd " +System.getProperty("user.dir")+ "\\" + projectName +
                    " && ..\\sonar-scanner-4.7.0.2747-windows\\bin\\sonar-scanner.bat");
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
                        "cd '" + System.getProperty("user.dir") +"/"+ projectName +"' ; ../sonar-scanner-4.7.0.2747-linux/bin/sonar-scanner");
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
        while (!isFinishedAnalyzing(projectOwner, projectName)) {
            Thread.sleep(1000);
        }
        Thread.sleep(500);
    }

    private boolean isFinishedAnalyzing(String projectOwner, String projectName) {
        boolean finished=false;
        try {
            URL url = new URL("http://localhost:9000/api/ce/activity?component="+projectOwner+":"+projectName);
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

    private void getMetricsFromSonarQube(String projectOwner, String projectName) throws IOException {
        try {
            URL url = new URL("http://localhost:9000/api/ce/activity?component="+projectOwner+":"+projectName+
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

                for(int i=0;i<jsonarr_1.size();i++){
                    JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
                    String metric = (String) jsonobj_1.get("metric");
                    String value = (String) jsonobj_1.get("value");
                    System.out.println(metric+" : "+value);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
