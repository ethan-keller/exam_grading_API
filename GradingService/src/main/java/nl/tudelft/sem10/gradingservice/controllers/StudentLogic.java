package nl.tudelft.sem10.gradingservice.controllers;

import nl.tudelft.sem10.gradingservice.entities.Grade;
import nl.tudelft.sem10.gradingservice.repositories.GradeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class StudentLogic {

    @Autowired
    private GradeRepository gradeRepository; // NOPMD

    public static float getMean(List<Grade> list) {
        float sum = 0;
        for (Grade g : list) {
            sum = sum + g.getMark();
        }
        sum = sum / list.size();

        return sum;
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
    public static double getGrade(List<Grade> list, String courseCode) throws JSONException {
        double g = 0.0;
        for (Grade grade : list) {
            String str = ServerCommunication.getCourseWeights(courseCode, grade.getGradeType());
            if (str == null) {
                return 0.0;
            }
            JSONObject obj = new JSONObject(str);
            if (obj.has("weight")) {
                double weight = obj.getDouble("weight");
                g = g + (grade.getMark() * weight);
            } else {
                g = -1;
            }
        }
        return g;
    }

    public static double getVariance(List<Double> grades, double mean) {
        double square = 0;
        for (double a : grades) {
            square += (a - mean) * (a - mean);
        }
        return square / grades.size();
    }

}
