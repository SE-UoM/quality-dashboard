package gr.uom.strategicplanning.analysis;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * The HttpClient class provides utility methods for making HTTP requests and parsing JSON responses.
 */
@Getter @Setter
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

        Response response;

        try {
            response = client
                    .newCall(request)
                    .execute();

            if (response.body() == null) {
                throw new IOException("Response body is null");
            }

            return response;
        }
        catch (IOException e) {
            throw new IOException("Failed to send GET request to " + url, e);
        }
    }

    public Response sentPostAuthRequest(String url) throws IOException {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        Response response;

        try {
            response = client
                    .newCall(request)
                    .execute();

            if (response.body() == null) {
                throw new IOException("Response body is null");
            }

            response.body().close();
            return response;
        }
        catch (IOException e) {
            throw new IOException("Failed to send POST request to " + url, e);
        }
    }

    public JSONObject convertResponseToJson(Response response) throws IOException {
        Map<String, Object> jsonMap = this.gson.fromJson(response.body().string(), Map.class);
        JSONObject jsonObject = new JSONObject(jsonMap);

        // Close the response body
        response.body().close();

        return jsonObject;
    }
}