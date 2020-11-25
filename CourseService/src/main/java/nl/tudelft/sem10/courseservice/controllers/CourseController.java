package nl.tudelft.sem10.courseservice.controllers;

import java.util.List;
import nl.tudelft.sem10.courseservice.entities.Course;
import nl.tudelft.sem10.courseservice.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    /*NOPMD*/CourseRepository courseRepository;

    /**
     * TODO: Insert Javadoc here.
     */

    @RequestMapping(method = RequestMethod.GET)
    //@GetMapping("/courses")
    @ResponseBody
    public List<Course> getAllCourses() {
        //public String getAllCourses() {
        List<Course> courseList = courseRepository.findAll();
        //Comparator<Course> comp = Comparator.comparing(Building::getBuildingName);
        //buildingList.sort(comp);
        //return "hello world";
        return courseList;
    }

}
