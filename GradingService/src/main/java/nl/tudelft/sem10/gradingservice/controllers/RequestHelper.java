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

    /**
     * Method that builds a getRequest to course service given a path.
     *
     * @param path path of the endpoint
     * @return the httprequest that has to be sent
     */
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

    /**
     * Method that sends the request to the server.
     *
     * @param request request that needs to be sent
     * @param client client sending the request
     * @return response of request
     */
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

