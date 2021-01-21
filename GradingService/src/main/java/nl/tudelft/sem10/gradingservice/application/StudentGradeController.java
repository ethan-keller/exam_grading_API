package nl.tudelft.sem10.gradingservice.application;

import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/student")
@SuppressWarnings("unused")
public class StudentGradeController {
    private static final String headerName = "Authorization";
    private final transient String userType = "STUDENT";
    @Autowired
    private GradeRepository gradeRepository; // NOPMD
    @Autowired
    private transient UserGradeService userService;
    private transient ServerCommunication serverCommunication = new ServerCommunication();

    public StudentGradeController() {
    }

    public void setGradeRepository(
        GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public void setUserService(UserGradeService userService) {
        this.userService = userService;
    }

    public void setServerCommunication(
        ServerCommunication serverCommunication) {
        this.serverCommunication = serverCommunication;
    }

    /**
     * Method to get mean grade of a student.
     *
     * @param netId netid of student
     * @return float of mean
     */
    @GetMapping(path = "/mean")
    @ResponseBody
    public ResponseEntity<Float> getMean(@RequestHeader(headerName)
                                             String token,
                                         @RequestParam String netId) {
        try {
            String str = serverCommunication.validate(token.substring(7));
            boolean correctUser = serverCommunication.validateUser(token.substring(7), netId);
            if (str.contains(userType) && correctUser) {
                return new ResponseEntity<>(userService.getMean(netId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<Double> getGrade(@RequestHeader(headerName)
                                               String token,
                                           @RequestParam String netId,
                                           @RequestParam String courseCode) throws JSONException {
        try {
            String str = serverCommunication.validate(token.substring(7));
            boolean correctUser = serverCommunication.validateUser(token.substring(7), netId);
            if (str.contains(userType) && correctUser) {
                return userService.getGrade(netId, courseCode, token);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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
    public ResponseEntity<List<String>> passedCourses(@RequestHeader(headerName)
                                                          String token,
                                                      @RequestParam String netId)
        throws JSONException {
        String str = serverCommunication.validate(token.replaceFirst("^Bearer ", ""));
        boolean correctUser =
            serverCommunication.validateUser(token.replaceFirst("^Bearer ", ""), netId);
        if (str.contains(userType) && correctUser) {
            return userService.passedCourses(netId, token);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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
    public ResponseEntity<List<String>> allGrades(@RequestHeader(headerName)
                                                      String token,
                                                  @RequestParam String netId) throws JSONException {
        try {
            String str = serverCommunication.validate(token.substring(7));
            boolean correctUser = serverCommunication.validateUser(token.substring(7), netId);
            if (str.contains(userType) && correctUser) {
                return userService.allGrades(netId, token);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<Double> passingRate(@RequestHeader(headerName)
                                                  String token,
                                              @RequestParam String course) throws JSONException {
        String str = serverCommunication.validate(token.replaceFirst("^Bearer ", ""));
        if (str.contains(userType)) {
            return userService.passingRate(course, token);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
