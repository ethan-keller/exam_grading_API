package nl.tudelft.sem10.gradingservice.application;

import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/grade")
@SuppressWarnings("unused")
public class GradeController {

    private static ServerCommunication serverCommunication = new ServerCommunication();
    @Autowired
    private GradeRepository gradeRepository; //NOPMD
    @Autowired
    private transient UserGradeService userService;

    public void setGradeRepository(
        GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public void setUserService(UserGradeService userService) {
        this.userService = userService;
    }

    public void setServerCommunication(
        ServerCommunication serverCommunication) {
        GradeController.serverCommunication = serverCommunication;
    }

    /**
     * Returns a list of all grade entities in the database.
     * Possibility of filtering on either netid, course, both or none.
     *
     * @return the list of all grades in the database
     */
    @RequestMapping(method = RequestMethod.GET)
    @GetMapping("grade")
    @ResponseBody
    public ResponseEntity<List<Grade>> getAllGrades(@RequestHeader("Authorization")
                                                        String token,
                                                    @RequestParam(required = false)
                                                        String netid,
                                                    @RequestParam(required = false)
                                                        String courseCode,
                                                    @RequestParam(required = false)
                                                        String gradeType) {
        try {
            String str = serverCommunication.validate(token.substring(7));
            if (str.contains("TEACHER")) {
                return userService.getAllGrades(netid, courseCode, gradeType);
            } else if (str.contains("STUDENT")) {
                if (serverCommunication.validateUser(token, netid)) {
                    return userService.getAllGrades(netid, courseCode, gradeType);
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
