package nl.tudelft.sem10.gradingservice.application;

import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/grade")
@SuppressWarnings("unused")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository; //NOPMD
    @Autowired
    private transient UserGradeService userService;

    /**
     * Returns a list of all grade entities in the database.
     * Possibility of filtering on either netid, course, both or none.
     *
     * @return the list of all grades in the database
     */
    @RequestMapping(method = RequestMethod.GET)
    @GetMapping("grade")
    @ResponseBody
    public ResponseEntity<List<Grade>> getAllGrades(@RequestParam(required = false) String netid,
                                                    @RequestParam(required = false)
                                                        String courseCode,
                                                    @RequestParam(required = false)
                                                        String gradeType) {
        return userService.getAllGrades(netid, courseCode, gradeType);
    }
}
