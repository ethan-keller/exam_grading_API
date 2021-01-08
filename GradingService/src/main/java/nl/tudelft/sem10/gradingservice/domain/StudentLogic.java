package nl.tudelft.sem10.gradingservice.domain;

import java.util.List;
import org.json.JSONException;


public class StudentLogic {


    // For testability
    private transient ServerCommunication serverCommunication =
        new ServerCommunication();

    public StudentLogic() {
    }

    public void setServerCommunication(ServerCommunication serverCommunication) {
        this.serverCommunication = serverCommunication;
    }

    /**
     * Method containing logic used to calculate grade.
     *
     * @param list       list of all grades a student had acquired for a course
     * @param courseCode course code of the course
     * @return double representing grade of course
     * @throws JSONException exception if json is wrong
     */
    @SuppressWarnings("PMD")
    public double getGrade(List<Grade> list, String courseCode, String token)
        throws JSONException {
        String jsonMap = serverCommunication.getCourseWeights(courseCode, token);
        if (jsonMap == null) {
            return -1.0;
        }
        return Utility.jsonWeightedAverage(jsonMap, list);
    }
}
