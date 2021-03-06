package nl.tudelft.sem10.courseservice.framework;

import nl.tudelft.sem10.courseservice.application.AuthService;
import nl.tudelft.sem10.courseservice.application.CourseService;
import nl.tudelft.sem10.courseservice.domain.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Course REST API.
 */
@Controller
@RequestMapping(path = "/teacher/course")
public class CourseController {
    private static final String RESPONSE_TYPE = "application/json";

    @Qualifier("getAuthService")
    @Autowired
    private AuthService auth; //NOPMD

    @Qualifier("getCourseService")
    @Autowired
    private CourseService courseService; //NOPMD

    /**
     * Get all available courses.
     *
     * @return all courses.
     */
    @GetMapping(path = "/courses", produces = RESPONSE_TYPE)
    public ResponseEntity<Iterable<Course>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.get());
    }

    /**
     * Get a course from the repository by ID.
     *
     * @param courseCode - String Course code.
     * @return the course or a 404 error if no such course exists.
     */
    @GetMapping(path = "/get", produces = RESPONSE_TYPE)
    public ResponseEntity<Course> getCourse(@RequestParam String courseCode) {
        Course course = courseService.get(courseCode);

        // The resource does not exist
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(course);
    }

    /**
     * Put a new course into the repository.
     *
     * @param course - Course Course to add.
     * @return the added course or a 409 error if a course with the same ID already exists.
     */
    @PostMapping(path = "/add", produces = RESPONSE_TYPE)
    public ResponseEntity<Course> addCourse(@RequestHeader(HttpHeaders.AUTHORIZATION)
                                                String authorization,
                                            @RequestBody
                                                Course course) {
        // Auth
        ResponseEntity<Course> authRes = authResponse(authorization);
        if (authRes != null) {
            return authRes;
        }

        Course result = courseService.add(course);

        // The resource already exists
        if (result == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Remove a course from the repository.
     *
     * @param courseCode - String Course code.
     * @return the deleted course or a 204 error.
     */
    @DeleteMapping(path = "/remove", produces = RESPONSE_TYPE)
    public ResponseEntity<Course> removeCourse(@RequestHeader(HttpHeaders.AUTHORIZATION)
                                                   String authorization,
                                               @RequestParam
                                                   String courseCode) {
        // Auth
        ResponseEntity<Course> authRes = authResponse(authorization);
        if (authRes != null) {
            return authRes;
        }

        Course result = courseService.remove(courseCode);

        // The resource does not exist
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Get a response entity for the authentication.
     * Note that this method only accepts {@link AuthService.UserType#TEACHER} users.
     *
     * @param token - String authorization token.
     * @param <T>   - Entity type.
     * @return the response or null if the token is valid.
     */
    private <T> ResponseEntity<T> authResponse(String token) {
        AuthService.UserType type = auth.getUser(token.replaceFirst("^Bearer ", ""));
        switch (type) {
            case TEACHER:
                return null;
            case UNKNOWN:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
                    .build();
            default:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }
}
