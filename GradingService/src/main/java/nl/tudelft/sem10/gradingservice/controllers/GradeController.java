package nl.tudelft.sem10.gradingservice.controllers;

import nl.tudelft.sem10.gradingservice.entities.Grade;
import nl.tudelft.sem10.gradingservice.repositories.GradeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    GradeRepository gradeRepository; //NOPMD

    /**
     * TODO: Insert Javadoc here.
     */

    @RequestMapping(method = RequestMethod.GET)
    @GetMapping("grade")
    @ResponseBody
    public List<Grade> getAllGrades() {
        List<Grade> gradeList = gradeRepository.findAll();
        Comparator<Grade> comp = Comparator.comparing(Grade::getNetid);
        gradeList.sort(comp);
        return gradeList;
    }

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

    @RequestMapping(value = "/{gradeId}", method = RequestMethod.DELETE)
    public void deleteGrade(@PathVariable final long gradeId) {
        gradeRepository.deleteGrade(gradeId);
    }

    @RequestMapping(value = "/{gradeId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateGrade(@RequestBody String jsonString, @PathVariable final long gradeId) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        float mark = (float) obj.getDouble("mark");

        assert gradeRepository.findById(gradeId).isPresent();
        Grade currGrade = gradeRepository.findById(gradeId).get();
        if (currGrade.getMark() < mark) {
            gradeRepository.updateGrade(gradeId, mark);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void insertGrade(@RequestBody String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        float mark = (float) obj.getDouble("mark");
        String netid = obj.getString("netid");

        String courseCode = obj.getString("course_code");
        int numberCount = 0;
        for (char c : courseCode.toCharArray()) {
            if (Character.isDigit(c)) {
                numberCount++;
            }
        }

        String gradeType = obj.getString("grade_type");
        if (numberCount == 4) {
            gradeRepository.insertGrade(mark, netid, courseCode, gradeType);
        }
    }


}
