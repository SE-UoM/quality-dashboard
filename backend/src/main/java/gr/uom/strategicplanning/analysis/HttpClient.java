package gr.uom.strategicplanning.analysis;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * The HttpClient class provides utility methods for making HTTP requests and parsing JSON responses.
 */
public abstract class HttpClient {
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();


    /**
     * Sends an HTTP GET request to the specified URL and returns the response body as a string.
     *
     * @param url the URL to send the GET request to
     * @return the response body as a string
     * @throws IOException if an I/O error occurs during the request
     */
    public String sendGetRequest(String url) throws IOException {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client
                .newCall(request)
                .execute();

        return response.body().string();
    }

}