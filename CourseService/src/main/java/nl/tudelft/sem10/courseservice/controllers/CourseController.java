package nl.tudelft.sem10.courseservice.controllers;

import java.util.List;
import nl.tudelft.sem10.courseservice.entities.Course;
import nl.tudelft.sem10.courseservice.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Course REST API.
 */
@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    CourseRepository courseRepository; //NOPMD

    /**
     * Get all available courses.
     * @return all courses.
     */
    @GetMapping(path = "/courses", produces = "application/json")
    @ResponseBody
    public Iterable<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Get a course from the repository by ID.
     * @param courseId - long Course ID.
     * @return the course or a 404 error if no such course exists.
     */
    @GetMapping(path = "/get", produces = "application/json")
    public ResponseEntity<String> getCourse(@RequestParam long courseId) {

        // Get a course by ID or null if no such course exists
        Course course = courseRepository.findById(courseId).orElse(null);

        // Return the course if it exists or a 404 error
        // TODO: Return a JSON body
        return course == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(course.toString(), HttpStatus.OK);
    }

    /**
     * Put a new course into the repository.
     * @param course - Course Course to add.
     * @return the added course or a 226 error if a course with the same ID already exists.
     */
    @PostMapping(path = "/add", produces = "application/json")
    public ResponseEntity<String> addCourse(@RequestBody Course course) {

        // Get a course by ID or null if no such course exists
        Course target = courseRepository.findById(course.getId()).orElse(null);

        // No duplicate course
        if (target == null) {
            // Save the course
            courseRepository.save(course);

            // TODO: Return a JSON body
            return new ResponseEntity<>(course.toString(), HttpStatus.CREATED);
        }

        // A course with the given ID already exists
        return new ResponseEntity<>(HttpStatus.IM_USED);
    }
}
