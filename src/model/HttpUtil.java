package model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class HttpUtil {
    public static class HttpResponse {
        public static final String BodyHandlers = null;
        public final int statusCode;
        public final String body;

        public HttpResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
    }

    private final HttpClient client = HttpClient.newHttpClient();

    public HttpResponse sendRequest(URI uri, String method, String authHeader, String body) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(uri)
            .header("Authorization", authHeader);

        if ("POST".equalsIgnoreCase(method)) {
            builder.header("Content-Type", "application/json")
                   .POST(HttpRequest.BodyPublishers.ofString(body));
        } else if ("PUT".equalsIgnoreCase(method)) {
            builder.header("Content-Type", "application/json")
                   .PUT(HttpRequest.BodyPublishers.ofString(body));
        } else if ("DELETE".equalsIgnoreCase(method)) {
            builder.DELETE();
        } else {
            builder.GET();
        }

        HttpRequest request = builder.build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        return new HttpResponse(response.statusCode(), response.body());
    }
}