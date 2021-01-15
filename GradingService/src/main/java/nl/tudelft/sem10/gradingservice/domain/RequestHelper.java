package nl.tudelft.sem10.gradingservice.domain;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Simplify cross microservice communication.
 */
public class RequestHelper {

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
    public HttpRequest getRequest(String path, String token, String params) {
        return HttpRequest
            .newBuilder()
            .GET()
            .header("Authorization", token)
            .uri(URI.create(path + params))
            .build();
    }

    /**
     * Method that sends the request to the server.
     *
     * @param request request that needs to be sent
     * @param client  client sending the request
     * @return response of request
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public String sendRequest(HttpRequest request, HttpClient client) {
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "Communication with server failed";
        }
        String responseBody = response.body();
        if (responseBody == null || responseBody.isEmpty()) {
            return "" + response.statusCode();
        }
        return responseBody;
    }

}