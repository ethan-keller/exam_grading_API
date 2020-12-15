package nl.tudelft.sem10.gradingservice.application;

import javassist.NotFoundException;
import nl.tudelft.sem10.gradingservice.domain.StudentLogic;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.GradeRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/teacher")
public class TeacherGradeController {

    @Autowired
    private GradeRepository gradeRepository; // NOPMD
    @Autowired
    private transient UserGradeService userService;
    private transient StudentLogic studentLogic;

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
        return userService.passingRate(course);
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
        return userService.meanAndVariance(course);
    }

    /**
     * updates a grade in the database if the newly given mark is higher than the stored one.
     *
     * @param netid      netid of student
     * @param courseCode course where the grade is from
     * @param gradeType  type of grade (midterm, ...)
     * @param jsonString body of request
     * @throws JSONException     if input format is wrong
     * @throws NotFoundException if grade is not found in database
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public void updateGrade(@RequestParam String netid,
                            @RequestParam String courseCode,
                            @RequestParam String gradeType,
                            @RequestBody String jsonString)
        throws JSONException, NotFoundException {
        userService.updateGrade(netid, courseCode, gradeType, jsonString);
    }

    /**
     * deletes a grade from the database based on given params
     *
     * @param netid      netid of student whose grade needs to be deleted
     * @param courseCode course from which grade needs to be deleted
     * @param gradeType  type of grades that needs to be deleted
     * @throws NotFoundException if grade is not found in the database based on the given parameters
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteGrade(@RequestParam String netid,
                            @RequestParam String courseCode,
                            @RequestParam String gradeType) throws NotFoundException {
        userService.deleteGrade(netid, courseCode, gradeType);
    }

    /**
     * inserts a grade into the database
     *
     * @param jsonString body of post message
     * @throws JSONException if input format is wrong
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("PMD")
    public void insertGrade(@RequestBody String jsonString) throws JSONException {
        userService.insertGrade(jsonString);
    }

}
