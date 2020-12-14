package nl.tudelft.sem10.gradingservice.domain;

import java.net.http.HttpClient;

/**
 * Simple class to streamline communication with other services.
 */
public class ServerCommunication {
    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Communicates to the course microservice and finds
     * the weights of grading categories of a course.
     *
     * @param courseCode course code of the course
     * @param categoryName the specific category whose weight is needed
     * @return JSON of the category and its weight
     */
    public static String getCourseWeights(String courseCode, String categoryName) {
        String str = RequestHelper.sendRequest(
                RequestHelper.getRequest("/teacher/category/get?courseCode=" + courseCode
                        + "&categoryName=" + categoryName), client);
        return str;
    }

}
