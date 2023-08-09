package gr.uom.strategicplanning.analysis;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * The HttpClient class provides utility methods for making HTTP requests and parsing JSON responses.
 */
public abstract class HttpClient {
    private OkHttpClient client = new OkHttpClient();
    protected Gson gson = new Gson();

    /**
     * Sends an HTTP GET request to the specified URL and returns the response body as a string.
     *
     * @param url the URL to send the GET request to
     * @return the response body as a string
     * @throws IOException if an I/O error occurs during the request
     */
    public Response sendGetRequest(String url) throws IOException {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client
                .newCall(request)
                .execute();

        return response;
    }

    public Response sentPostAuthRequest(String url) throws IOException {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        Response response = client
                .newCall(request)
                .execute();

        return response;
    }

    public JSONObject convertResponseToJson(Response response) throws IOException {
        Map<String, Object> jsonMap = this.gson.fromJson(response.body().string(), Map.class);
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject;
    }
}