package nl.tudelft.sem10.gradingservice.domain;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Simplify cross microservice communication.
 */
public class RequestHelper {

    private static final String domainOfCourseService = "http://localhost:8081";
    private static final String domainOfAuthenticationService = "http://localhost:8080";

    /**
     * Basic constructor.
     */
    public RequestHelper() {
    }

    /**
     * Method that builds a getRequest to course service given a path.
     *
     * @param path path of the endpoint
     * @return the httprequest that has to be sent
     */
    public HttpRequest getRequest(String path, String token) {

        String reqPath = domainOfCourseService + path;
        return HttpRequest
            .newBuilder()
            .GET()
            .header("Authorization", token)
            .uri(URI.create(reqPath))
            .build();
    }

    /**
     * Method that builds a get request to authentication service /validate.
     *
     * @param token token user sends
     * @return hhtprequest to send
     */
    public HttpRequest validateToken(String token) {
        String reqPath = domainOfAuthenticationService;
        return HttpRequest
            .newBuilder()
            .GET()
            .uri(URI.create(reqPath + "/validate/" + token))
            .build();
    }

    /**
     * Method to build a get request to authentication service's validateNetIdToken endpoint.
     *
     * @param netId - the provided netId
     * @param token - token of the user
     *
     * @return HttpRequest
     */
    public HttpRequest validateNetIdToken(String netId, String token) {
        String reqPath = domainOfAuthenticationService;
        return HttpRequest
                .newBuilder()
                .GET()
                .header("Authorization", token)
                .uri(URI.create(reqPath + "/validate/netId/" + netId))
                .build();
    }

    /**
     * Method that sends the request to the server.
     *
     * @param request request that needs to be sent
     * @param client  client sending the request
     * @return response of request
     */
    public String sendRequest(HttpRequest request, HttpClient client) {
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