package nl.tudelft.sem10.courseservice;

import java.util.Iterator;
import nl.tudelft.sem10.courseservice.application.CourseServiceImpl;
import nl.tudelft.sem10.courseservice.domain.model.Course;
import nl.tudelft.sem10.courseservice.domain.repository.CourseRepository;
import nl.tudelft.sem10.courseservice.framework.CourseController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests for all {@link CourseController} endpoints.
 * Note that these tests assume the implementation {@link CourseServiceImpl} is used.
 * Since the controller wraps all {@link nl.tudelft.sem10.courseservice.application.CourseService}
 * methods its implementation is also fully tested.
 */
@SpringBootTest(classes = CourseServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseServiceApplicationTests {
    private final transient Course c0 = new Course("TEST_COURSE", "CSE9999");
    private final transient String validToken = "Bearer MyTeacherToken";

    @Autowired
    private transient CourseController controller;

    /**
     * Mock the repository so no database connection is required.
     * We assume the controller and its service are autowired correctly.
     */
    @BeforeAll
    public void setup() throws ReflectiveOperationException {
        Object service = Util.getField(CourseController.class, "courseService").get(controller);

        // Only for the CourseService implementation do we inject a mock repository
        if (!(service instanceof CourseServiceImpl)) {
            return;
        }

        // Inject mock into controller
        // Yes this is really ugly but at least PMD will not complain
        Util.setField(service,
                "courseRepository",
                Util.repositoryMock(
                        CourseRepository.class,
                        Course.class,
                        String.class,
                        Course::getCode));

        // Inject auth mock
        Util.setField(controller, "auth", Util.mockAuth());
    }

    /**
     * Test /course/get/ response for a non existent course.
     */
    @Test
    @Order(1)
    public void testGetNonExistent() {
        ResponseEntity<Course> response = controller.getCourse(c0.getCode());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test /course/add/ response adding a new course.
     */
    @Test
    @Order(2)
    public void testAdd() {
        // Note that we assume no such course exists, as the test will delete the test course too
        ResponseEntity<Course> response = controller.addCourse(validToken, c0);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/add/ response adding a course with duplicate ID.
     */
    @Test
    @Order(3)
    public void testAddDuplicate() {
        ResponseEntity<Course> response = controller.addCourse(validToken, c0);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Test /course/get/ response for an existing course.
     */
    @Test
    @Order(4)
    public void testGet() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Course> response = controller.getCourse(c0.getCode());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/courses/ response for an existing course.
     */
    @Test
    @Order(5)
    public void testGetAll() {
        // Note that only one entity (c0) should exist, as we inserted it in a previous test
        ResponseEntity<Iterable<Course>> response = controller.getAllCourses();

        Iterable<Course> iterable = response.getBody();

        // Response has a body
        Assertions.assertNotNull(iterable);

        // This should not be null by contract
        Iterator<Course> iterator = iterable.iterator();

        // Course c0
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(c0, iterator.next());

        // Only one element so there should not be a next one
        Assertions.assertFalse(iterator.hasNext());
    }

    /**
     * Test /course/remove/ response for an existing course.
     */
    @Test
    @Order(6)
    public void testRemove() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Course> response = controller.removeCourse(validToken, c0.getCode());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/remove/ response for a non existent course.
     */
    @Test
    @Order(7)
    public void testRemoveNonExisting() {
        ResponseEntity<Course> response = controller.removeCourse(validToken, c0.getCode());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test /course/add/ response for a non-teacher user.
     */
    @Test
    @Order(8)
    public void testAddWrongUser() {
        ResponseEntity<Course> response = controller.addCourse("Bearer MyNonTeacherToken", c0);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test /course/add/ response for an unknown user.
     */
    @Test
    @Order(9)
    public void testAddInvalidUser() {
        ResponseEntity<Course> response = controller.addCourse("Bearer MyInvalidToken", c0);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Bearer",
                response.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE));
    }

    /**
     * Test /course/remove/ response for a non-teacher user.
     */
    @Test
    @Order(10)
    public void testRemoveWrongUser() {
        ResponseEntity<Course> response = controller.removeCourse("Bearer MyNonTeacherToken",
                "CSE9999");

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test /course/remove/ response for an unknown user.
     */
    @Test
    @Order(11)
    public void testRemoveInvalidUser() {
        ResponseEntity<Course> response = controller.removeCourse("Bearer MyInvalidToken",
                "CSE9999");

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Bearer",
                response.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE));
    }
}
