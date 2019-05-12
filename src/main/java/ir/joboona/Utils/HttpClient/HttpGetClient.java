package ir.joboona.Utils.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetClient {
    private static HttpGetClient instance;

    private HttpGetClient() {
    }

    public static HttpGetClient getInstance() {
        if (instance == null)
            instance = new HttpGetClient();
        return instance;
    }

    public String getRequest(String urlToRead, int timeout) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(timeout);
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();

    }
}
