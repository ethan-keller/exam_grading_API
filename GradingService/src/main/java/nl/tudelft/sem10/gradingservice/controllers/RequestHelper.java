package nl.tudelft.sem10.gradingservice.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * Simplify cross microservice communication.
 */
public class RequestHelper {

    private static String username;
    private static String password;
    private static Integer type;
    private static final String domainOfCourseService = "http://localhost:8081";


    public static HttpRequest getRequest(String path) {

        String reqPath = domainOfCourseService + path;
        //String userAndPass = username + ":" + password;
        //String basicAuthPayload = "Basic "
        // + Base64.getEncoder().encodeToString(userAndPass.getBytes());
        //.header("Authorization", basicAuthPayload)
        return HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(reqPath))
                .build();
    }


    public static String sendRequest(HttpRequest request, HttpClient client) {
        try {
            HttpResponse<String> response;
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.body().isEmpty()) {
                return Integer.toString(response.statusCode());
            }
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
    }




}

