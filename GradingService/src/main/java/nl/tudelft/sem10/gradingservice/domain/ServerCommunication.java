package nl.tudelft.sem10.gradingservice.domain;

import java.net.http.HttpClient;

/**
 * Simple class to streamline communication with other services.
 */
public class ServerCommunication {

    public ServerCommunication() {}

    private static final HttpClient client = HttpClient.newBuilder().build();

    private transient RequestHelper requestHelper = new RequestHelper();

    public void setRequestHelper(RequestHelper requestHelper) {
        this.requestHelper = requestHelper;
    }

    /**
     * Communicates to the course microservice and finds
     * the weights of grading categories of a course.
     *
     * @param courseCode   course code of the course
     * @param categoryName the specific category whose weight is needed
     * @return JSON of the category and its weight
     */
    public String getCourseWeights(String courseCode, String categoryName, String token) {
        String str = requestHelper.sendRequest(
                requestHelper.getRequest("/teacher/category/get?courseCode=" + courseCode
                        + "&categoryName=" + categoryName, token), client);
        return str;
    }

    /**
     * Communicates with the authentication microservice
     * and returns if a given token is valid and in the case it is valid
     * the type of the user who sent the token.
     *
     * @param token token user sends
     * @return type of user if applicable
     */
    public String validate(String token) {
        String str = requestHelper.sendRequest(
                requestHelper.validateToken(token), client);
        return str;
    }

    /*
    TODO: add real logic
     */
    public boolean validateUser(String token, String netId) {
        //Needs to add stuff here
        return true;
    }
}
