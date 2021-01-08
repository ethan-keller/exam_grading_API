package nl.tudelft.sem10.gradingservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;


public class StudentLogic {


    // For testability
    private transient ServerCommunication serverCommunication =
        new ServerCommunication();

    public StudentLogic() {
    }

    /**
     * Logic to get the mean of a list of grades.
     *
     * @param list list of grades
     * @return mean of grades
     */
    public static float getMean(List<Grade> list) {
        float sum = 0;
        for (Grade g : list) {
            sum = sum + g.getMark();
        }
        sum = sum / list.size();

        return sum;
    }

    /**
     * Gets the variance given a list of grades and a mean of the grades.
     *
     * @param grades list of grades
     * @param mean   mean of grades
     * @return variance of the grades
     */
    public static double getVariance(List<Double> grades, double mean) {
        double square = 0;
        for (double a : grades) {
            square += (a - mean) * (a - mean);
        }
        return square / grades.size();
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

        List<Double> grades = list.stream().map(x -> (double)x.getMark()).collect(Collectors.toList());
        List<Double> weights = new ArrayList<>();
        for (Grade grade : list) {
            String str = serverCommunication.getCourseWeights(courseCode,
                grade.getGradeType(), token);
            if (str == null) {
                return 0.0;
            }
            JSONObject obj = Utility.stringToJson(str);
            if (obj.has("weight")) {
                double weight = obj.getDouble("weight");
                weights.add(weight);
            } else {
                return -1;
            }
        }
        return Utility.weightedAverage(grades, weights);
    }

}
