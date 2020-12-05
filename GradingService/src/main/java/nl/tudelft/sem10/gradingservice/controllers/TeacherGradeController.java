package nl.tudelft.sem10.gradingservice.controllers;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem10.gradingservice.entities.Grade;
import nl.tudelft.sem10.gradingservice.repositories.GradeRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/teacher")
public class TeacherGradeController {

    @Autowired
    private GradeRepository gradeRepository; // NOPMD
    private static double passingGrade = 5.75;
    private transient ServerCommunication serverCommunication = new ServerCommunication();
    private transient StudentGradeController studentGradeController = new StudentGradeController();

    /**
     * NOT REAL JAVADOC
     * This method is currently just a copy of the passing rate method in student controller.
     * Right now kept here since it works.
     * TODO: improve code quality (no copy paste methods)
     *
     * @param course ...
     * @return ...
     * @throws JSONException ...
     */
    @SuppressWarnings("PMD")
    @GetMapping(path = "/passingRate")
    @ResponseBody
    public ResponseEntity<Double> passingRate(@RequestParam String course) throws JSONException {
        List<String> students = gradeRepository.getStudentsTakingCourse(course);
        if (students == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double passCount = 0.0;
        for (String netId : students) {
            List<Grade> list =
                    gradeRepository.getGradesByNetIdAndCourse(netId, course);
            if (list == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            double grade = studentGradeController.getGrade(list, course);
            if (grade > passingGrade) {
                passCount++;
            }
        }
        double passRate = passCount / students.size();
        return new ResponseEntity<>(passRate, HttpStatus.OK);
    }

    /**
     * Method to get the statistics such as mean and variance of final grades of a course.
     *
     * @param course course code whose statistics are needed
     * @return JSON containing the statistics
     * @throws JSONException json exception if wrong json
     */
    @SuppressWarnings("PMD")
    @GetMapping(path = "/statistics")
    @ResponseBody
    public ResponseEntity<String> meanAndVariance(@RequestParam String course)
            throws JSONException {
        List<String> students = gradeRepository.getStudentsTakingCourse(course);
        List<Double> grades = new ArrayList<>(students.size());
        if (students == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double sum = 0.0;
        for (String netId : students) {
            List<Grade> list =
                    gradeRepository.getGradesByNetIdAndCourse(netId, course);
            if (list == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            double g = studentGradeController.getGrade(list, course);
            grades.add(g);
            sum = sum + g;
        }
        sum = sum / students.size();
        double variance = getVariance(grades, sum);
        String json =  "{\"mean\":\"" + sum + "\", \"variance\":\"" + variance + "\"}";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    double getVariance(List<Double> grades, double mean) {
        double square = 0;
        for (double a : grades) {
            square += (a - mean) * (a - mean);
        }
        return square / grades.size();
    }

}
