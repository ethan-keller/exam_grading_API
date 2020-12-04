package nl.tudelft.sem10.gradingservice.controllers;

import nl.tudelft.sem10.gradingservice.entities.Category;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

public class ServerCommunication {
    static RequestHelper requestHelper = new RequestHelper();
    private static HttpClient client = HttpClient.newBuilder().build();

    public static void main(String[] args) {
        System.out.println(getCourseWeights());
    }

    public static String getCourseWeights() {
        String str = RequestHelper.sendRequest(
                RequestHelper.getRequest("/teacher/category/categories"), client);
        return str;
    }

}
