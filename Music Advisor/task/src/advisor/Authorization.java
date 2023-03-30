package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Authorization {
    public static void createHttpServer() {
        try {
            String uri = Config.SERVER_PATH + "/authorize" +
                    "?client_id=" + Config.CLIENT_ID +
                    "&redirect_uri=" + Config.REDIRECT_URI +
                    "&response_type=code";
            System.out.println("use this link to request the access code:\n" + uri);
            System.out.println("waiting for code...");

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", exchange -> {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("code")) {
                    Config.AUTH_CODE = query.substring(5);
                    query = "Got the code. Return back to your program.";
                } else {
                    query = "Authorization code not found. Try again.";
                }
                exchange.sendResponseHeaders(200, query.length());
                exchange.getResponseBody().write(query.getBytes());
                exchange.getResponseBody().close();
            });
            server.start();
            while (Config.AUTH_CODE.equals("")) {
                Thread.sleep(10);
            }
            server.stop(10);
            authRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void authRequest() {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Config.SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code"
                                + "&code=" + Config.AUTH_CODE
                                + "&client_id=" + Config.CLIENT_ID
                                + "&client_secret=" + Config.CLIENT_SECRET
                                + "&redirect_uri=" + Config.REDIRECT_URI))
                .build();
        System.out.println("making http request for access_token...");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null && response.body().contains("access_token")) { parseAccessToken(response.body()); }
            assert  response != null;
            System.out.println(response.body());
            if (!Config.AUTH_CODE.equals("")) {
                System.out.println( "---SUCCESS---");
                Config.AUTHORIZATION = true;
            }
        } catch (InterruptedException | IOException e) { System.out.println("Error response"); }
    }
    static void parseAccessToken(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Config.ACCESS_TOKEN = jo.get("access_token").getAsString();
    }

    static String getResponse(String uriEnding) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Config.ACCESS_TOKEN)
                .uri(URI.create(Config.RESOURCE + uriEnding))
                .GET()
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            return "Error";
        }
    }
}