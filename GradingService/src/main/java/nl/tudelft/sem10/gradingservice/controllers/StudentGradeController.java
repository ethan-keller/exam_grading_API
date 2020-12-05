package nl.tudelft.sem10.gradingservice.controllers;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem10.gradingservice.entities.Grade;
import nl.tudelft.sem10.gradingservice.repositories.GradeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/student")
@SuppressWarnings("unused")
public class StudentGradeController {

    @Autowired
    private GradeRepository gradeRepository; // NOPMD
    private static double passingGrade = 5.75;
    private transient ServerCommunication serverCommunication = new ServerCommunication();

    /**
     * Method to get mean grade of a student.
     *
     * @param netId netid of student
     * @return float of mean
     */
    @GetMapping(path = "/mean")
    @ResponseBody
    public ResponseEntity<Float> getMean(@RequestParam String netId) {
        List<Grade> list = gradeRepository.getGradesByNetId(netId);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        float sum = 0;
        for (Grade g : list) {
            sum = sum + g.getMark();
        }
        sum = sum / list.size();
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }

    @GetMapping(path = "/grade")
    @ResponseBody
    public ResponseEntity<Double> getGrade(@RequestParam String netId,
                                           @RequestParam String courseCode) throws JSONException {
        List<Grade> list =
                gradeRepository.getGradesByNetIdAndCourse(netId, courseCode);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double g = getGrade(list, courseCode);
        return new ResponseEntity<>(g, HttpStatus.ACCEPTED);
    }

    private double getGrade(List<Grade> list, String courseCode) throws JSONException {
        double g = 0.0;
        for (Grade grade : list) {
            String str = serverCommunication.getCourseWeights(courseCode, grade.getGradeType());
            JSONObject obj = new JSONObject(str);
            double weight = obj.getDouble("weight");
            g = g + (grade.getMark() * weight);
        }
        return g;
    }

    @GetMapping(path = "/passed")
    @ResponseBody
    public ResponseEntity<List<String>> passedCourses(@RequestParam String netId)
            throws JSONException {
        List<String> list = gradeRepository.getCoursesOfStudent(netId);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> passed = new ArrayList<>();
        for (String course : list) {
            List<Grade> l = gradeRepository.getGradesByNetIdAndCourse(netId, course);
            double grade = getGrade(l, course);
            if (grade >= passingGrade) {
                passed.add(course);
            }
        }

        return new ResponseEntity<>(passed, HttpStatus.OK);
    }

    @GetMapping(path = "/allGrades")
    @ResponseBody
    public ResponseEntity<List<Double>> allGrades(@RequestParam String netId) throws JSONException {
        List<String> list = gradeRepository.getCoursesOfStudent(netId);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Double> gr = new ArrayList<>();
        for (String course : list) {
            List<Grade> l = gradeRepository.getGradesByNetIdAndCourse(netId, course);
            gr.add(getGrade(l, course));
        }

        return new ResponseEntity<>(gr, HttpStatus.OK);
    }

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
            double grade = getGrade(list, course);
            if (grade > passingGrade) {
                passCount++;
            }
        }
        double passRate = passCount / students.size();
        return new ResponseEntity<>(passRate, HttpStatus.ACCEPTED);
    }
}
