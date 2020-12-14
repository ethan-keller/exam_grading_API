package nl.tudelft.sem10.gradingservice.application;

import javassist.NotFoundException;
import nl.tudelft.sem10.gradingservice.domain.StudentLogic;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.GradeRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public void updateGrade(@RequestParam String netid,
                            @RequestParam String courseCode,
                            @RequestParam String gradeType,
                            @RequestBody String jsonString) throws JSONException, NotFoundException {
        userService.updateGrade(netid, courseCode, gradeType, jsonString);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteGrade(@RequestParam String netid,
                            @RequestParam String courseCode,
                            @RequestParam String gradeType) throws NotFoundException {
        userService.deleteGrade(netid, courseCode, gradeType);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("PMD")
    public void insertGrade(@RequestBody String jsonString) throws JSONException {
        userService.insertGrade(jsonString);
    }

}
