package nl.tudelft.sem10.gradingservice.application;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.repositories.GradeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grade")
@SuppressWarnings("unused")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository; //NOPMD

    /**
     * Returns a list of all grade entities in the database.
     * Possibility of filtering on either netid, course, both or none.
     *
     * @return the list of all grades in the database
     */

    @RequestMapping(method = RequestMethod.GET)
    @GetMapping("grade")
    @ResponseBody
    public List<Grade> getAllGrades(@RequestParam(required = false) String netid,
                                    @RequestParam(required = false) String courseCode) {
        List<Grade> gradeList;
        if (netid == null && courseCode == null) {
            gradeList = gradeRepository.findAll();
        } else if (netid != null && courseCode == null) {
            gradeList = gradeRepository.getGradesByNetId(netid);
        } else if (netid == null) {
            gradeList = gradeRepository.getGradesByCourse(courseCode);
        } else {
            gradeList = gradeRepository.getGradesByNetIdAndCourse(netid, courseCode);
        }
        return gradeList;
    }

    /**
     * return a grade from the database based on id of said grade in the database.
     *
     * @throws IllegalAccessException if grade isn't in the database.
     * @return grade with pathvariable id
     */
    @RequestMapping(value = "/{gradeId}", method = RequestMethod.GET)
    @ResponseBody
    public Grade getGrade(@PathVariable final long gradeId) throws IllegalAccessException {
        Optional<Grade> b = gradeRepository.findById(gradeId);
        if (b.isEmpty()) {
            throw new IllegalAccessException("Grade is not found");
        } else {
            return b.get();
        }
    }

    /**
     * Deletes grade from database based on id.
     *
     * @param gradeId path variable of grade to be deleted
     */
    @RequestMapping(value = "/{gradeId}", method = RequestMethod.DELETE)
    public void deleteGrade(@PathVariable final long gradeId) {
        gradeRepository.deleteGrade(gradeId);
    }

    /**
     * updates grade from database based on id if the updated value is higher than the previous one.
     *
     * @param jsonString body with the grade in it
     * @param gradeId grade to be updated
     * @throws JSONException if grade is not found in database
     */
    @RequestMapping(value = "/{gradeId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateGrade(@RequestBody String jsonString, @PathVariable final long gradeId)
        throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        float mark = (float) obj.getDouble("mark");

        assert gradeRepository.findById(gradeId).isPresent();
        Grade currGrade = gradeRepository.findById(gradeId).get();
        if (currGrade.getMark() < mark) {
            gradeRepository.updateGrade(gradeId, mark);
        }
    }

    /**
     * Inserts a new grade into the database.
     *
     * @param jsonString body of post request, contains all info for a new grade
     * @throws JSONException if something goes wrong while creating a JSON object
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("PMD")
    public void insertGrade(@RequestBody String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        String courseCode = obj.getString("course_code");
        int numberCount = 0;
        for (char c : courseCode.toCharArray()) {
            if (Character.isDigit(c)) {
                numberCount++;
            }
        }
        String gradeType = obj.getString("grade_type");
        if (numberCount == 4) {
            String netid = obj.getString("netid");
            float mark = (float) obj.getDouble("mark");
            gradeRepository.insertGrade(mark, netid, courseCode, gradeType);
        }
    }
}
