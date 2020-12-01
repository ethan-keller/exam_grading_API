package nl.tudelft.sem10.courseservice.controllers;

import nl.tudelft.sem10.courseservice.entities.Course;
import nl.tudelft.sem10.courseservice.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Course REST API.
 */
@Controller
@RequestMapping(path = "/course")
public class CourseController {
    private static final String RESPONSE_TYPE = "application/json";
    @Autowired
    CourseRepository courseRepository; //NOPMD

    /**
     * Get all available courses.
     *
     * @return all courses.
     */
    @GetMapping(path = "/courses", produces = RESPONSE_TYPE)
    public ResponseEntity<Iterable<Course>> getAllCourses() {
        return new ResponseEntity<>(courseRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Get a course from the repository by ID.
     *
     * @param courseId - long Course ID.
     * @return the course or a 404 error if no such course exists.
     */
    @GetMapping(path = "/get", produces = RESPONSE_TYPE)
    public ResponseEntity<Course> getCourse(@RequestParam long courseId) {

        // Get a course by ID or null if no such course exists
        Course course = courseRepository.findById(courseId).orElse(null);

        // Return the course if it exists or a 404 error
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    /**
     * Put a new course into the repository.
     *
     * @param course - Course Course to add.
     * @return the added course or a 409 error if a course with the same ID already exists.
     */
    @PostMapping(path = "/add", produces = RESPONSE_TYPE)
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {

        // Duplicate course
        if (courseRepository.existsById(course.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Save the course
        courseRepository.save(course);

        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    /**
     * Remove a course from the repository.
     *
     * @param courseId - long Course ID.
     * @return the deleted course or a 204 error.
     */
    @DeleteMapping(path = "/remove", produces = RESPONSE_TYPE)
    public ResponseEntity<Course> removeCourse(@RequestParam long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);

        // No such course
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Delete the course
        courseRepository.deleteById(courseId);

        // Return the deleted course
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}
