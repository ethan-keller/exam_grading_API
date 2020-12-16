package nl.tudelft.sem10.gradingservice.application;

import javassist.NotFoundException;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.StudentLogic;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller containing all endpoints available to user of type TEACHER only
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping("/teacher")
public class TeacherGradeController {

    @Autowired
    private GradeRepository gradeRepository; // NOPMD
    @Autowired
    private transient UserGradeService userService;
    // For dependency injection
    private static final ServerCommunication serverCommunication = new ServerCommunication();

    /**
     * Method that returns passing rates of a course if user requesting for it is a teacher.
     *
     * @param course course whose passing rate is needed
     * @return passing rate as a double
     * @throws JSONException jsonexception if json cannot be parsed
     */
    @SuppressWarnings("PMD")
    @GetMapping(path = "/passingRate")
    @ResponseBody
    public ResponseEntity<Double> passingRate(@RequestHeader("Authorization")
                                                      String token, @RequestParam String course)
            throws JSONException {
        try {
            String str = serverCommunication.validate(token.substring(7));
            if (str.contains("TEACHER")) {
                return userService.passingRate(course, token);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<String> meanAndVariance(@RequestHeader("Authorization")
                                                          String token,
                                                  @RequestParam String course)
            throws JSONException {
        try {
            String str = serverCommunication.validate(token.substring(7));
            if (str.contains("TEACHER")) {
                return userService.meanAndVariance(course, token);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception MissingRequestHeaderException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<String> updateGrade(@RequestHeader("Authorization")
                                                      String token,
                                              @RequestParam String netid,
                                              @RequestParam String courseCode,
                                              @RequestParam String gradeType,
                                              @RequestBody String jsonString) throws JSONException,
            NotFoundException {
        try {
            String str = serverCommunication.validate(token.substring(7));
            if (str.contains("TEACHER")) {
                userService.updateGrade(netid, courseCode, gradeType, jsonString);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception MissingRequestHeaderException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * deletes a grade from the database based on given params.
     *
     * @param netid      netid of student whose grade needs to be deleted
     * @param courseCode course from which grade needs to be deleted
     * @param gradeType  type of grades that needs to be deleted
     * @throws NotFoundException if grade is not found in the database based on the given parameters
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteGrade(@RequestHeader("Authorization")
                                                      String token,
                                              @RequestParam String netid,
                                              @RequestParam String courseCode,
                                              @RequestParam String gradeType)
            throws NotFoundException {
        try {
            String str = serverCommunication.validate(token.substring(7));
            if (str.contains("TEACHER")) {
                userService.deleteGrade(netid, courseCode, gradeType);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception MissingRequestHeaderException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * inserts a grade into the database.
     *
     * @param jsonString body of post message
     * @throws JSONException if input format is wrong
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("PMD")
    public ResponseEntity<String> insertGrade(@RequestHeader("Authorization")
                                                      String token,
                                              @RequestBody String jsonString) throws JSONException {
        try {
            String str = serverCommunication.validate(token.substring(7));
            if (str.contains("TEACHER")) {
                userService.insertGrade(jsonString);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception MissingRequestHeaderException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
