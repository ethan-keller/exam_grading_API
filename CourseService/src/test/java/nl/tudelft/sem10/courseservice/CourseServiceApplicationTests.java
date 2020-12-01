package nl.tudelft.sem10.courseservice;

import nl.tudelft.sem10.courseservice.controllers.CourseController;
import nl.tudelft.sem10.courseservice.entities.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * TODO: /course/courses (get all courses) endpoint testing.
 */
@SpringBootTest(classes = CourseServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseServiceApplicationTests {
    private final transient Course c0 = new Course(9999L, "TEST_COURSE", "CSE9999");

    @Autowired
    private transient CourseController controller;

    /**
     * Test if the controller is created.
     */
    @Test
    @Order(1)
    public void contextLoads() {
        Assertions.assertNotNull(controller);
    }

    /**
     * Test /course/get/ response for a non existent course.
     */
    @Test
    @Order(2)
    public void testGetNonExistent() {
        ResponseEntity<Course> response = controller.getCourse(c0.getId());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test /course/add/ response adding a new course.
     */
    @Test
    @Order(3)
    public void testAdd() {
        // Note that we assume no such course exists, as the test will delete the test course too
        ResponseEntity<Course> response = controller.addCourse(c0);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/add/ response adding a course with duplicate ID.
     */
    @Test
    @Order(4)
    public void testAddDuplicate() {
        ResponseEntity<Course> response = controller.addCourse(c0);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Test /course/get/ response for an existing course.
     */
    @Test
    @Order(5)
    public void testGet() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Course> response = controller.getCourse(c0.getId());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/remove/ response for an existing course.
     */
    @Test
    @Order(6)
    public void testRemove() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Course> response = controller.removeCourse(c0.getId());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/remove/ response for a non existent course.
     */
    @Test
    @Order(7)
    public void testRemoveNonExisting() {
        ResponseEntity<Course> response = controller.removeCourse(c0.getId());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
