package nl.tudelft.sem10.gradingservice.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem10.gradingservice.entities.Grade;
import nl.tudelft.sem10.gradingservice.repositories.GradeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/student")
@SuppressWarnings("unused")
public class StudentGradeController {

    @Autowired
    private GradeRepository gradeRepository; // NOPMD

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

}
