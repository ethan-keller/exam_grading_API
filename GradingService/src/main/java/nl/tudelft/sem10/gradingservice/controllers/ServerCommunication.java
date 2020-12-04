package nl.tudelft.sem10.gradingservice.controllers;

import java.net.http.HttpClient;

public class ServerCommunication {
    static RequestHelper requestHelper = new RequestHelper();
    private static HttpClient client = HttpClient.newBuilder().build();

    public static void main(String[] args) {
        System.out.println(getCourseWeights("CSE1", "A"));
    }

    public static String getCourseWeights(String courseCode, String categoryName) {
        String str = RequestHelper.sendRequest(
                RequestHelper.getRequest("/teacher/category/get?courseCode=" + courseCode
                        + "&categoryName=" + categoryName), client);
        return str;
    }

}
