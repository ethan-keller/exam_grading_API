package nl.tudelft.sem10.gradingservice.application;

import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.GradeRepository;
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
@RequestMapping("/student")
@SuppressWarnings("unused")
public class StudentGradeController {

    static final double passingGrade = 5.75;
    @Autowired
    private GradeRepository gradeRepository; // NOPMD

    @Autowired
    private transient UserGradeService userService;

    /**
     * Method to get mean grade of a student.
     *
     * @param netId netid of student
     * @return float of mean
     */
    @GetMapping(path = "/mean")
    @ResponseBody
    public ResponseEntity<Float> getMean(@RequestParam String netId) {
        return new ResponseEntity<>(userService.getMean(netId), HttpStatus.OK);
    }

    /**
     * Method to return grade of a specific student for a specific course.
     *
     * @param netId      netId of student
     * @param courseCode course code of the grade required
     * @return a double representing grade of that course
     * @throws JSONException exception if json is wrong
     */
    @GetMapping(path = "/grade")
    @ResponseBody
    public ResponseEntity<Double> getGrade(@RequestParam String netId,
                                           @RequestParam String courseCode) throws JSONException {
        return userService.getGrade(netId, courseCode);
    }

    /**
     * Method to return the course code of all courses a student has passed.
     *
     * @param netId netId of the student
     * @return A list of strings that are the course codes
     * @throws JSONException exception if json is wrong
     */
    @GetMapping(path = "/passed")
    @ResponseBody
    public ResponseEntity<List<String>> passedCourses(@RequestParam String netId)
        throws JSONException {
        return userService.passedCourses(netId);
    }


    /**
     * Method to get all the final grades a student has achieved for every course.
     *
     * @param netId netId of the student
     * @return List of json objects
     * @throws JSONException exception if json is wrong
     */
    @GetMapping(path = "/allGrades")
    @ResponseBody
    public ResponseEntity<List<String>> allGrades(@RequestParam String netId) throws JSONException {
        return userService.allGrades(netId);
    }

    /**
     * Get the passing rate of a course.
     *
     * @param course course code of the course
     * @return the passing rate as a double in between 0.0-1.0
     * @throws JSONException exception if json is wrong
     */
    @SuppressWarnings("PMD")
    @GetMapping(path = "/passingRate")
    @ResponseBody
    public ResponseEntity<Double> passingRate(@RequestParam String course) throws JSONException {
        return userService.passingRate(course);
    }

}
