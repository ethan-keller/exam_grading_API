package nl.tudelft.sem10.courseservice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import nl.tudelft.sem10.courseservice.controllers.CourseController;
import nl.tudelft.sem10.courseservice.entities.Course;
import nl.tudelft.sem10.courseservice.repositories.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests for all {@link CourseController} endpoints.
 */
@SpringBootTest(classes = CourseServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseServiceApplicationTests {
    private final transient Course c0 = new Course("TEST_COURSE", "CSE9999");

    @Autowired
    private transient CourseController controller;

    /**
     * Mock the repository so no database connection is required.
     */
    @BeforeAll
    public void setup() throws ReflectiveOperationException {
        Map<String, Course> map = new HashMap<>();
        CourseRepository mock = Mockito.mock(CourseRepository.class);

        // #findAll()
        Mockito.doAnswer(invocation -> {
            return Collections.unmodifiableList(new ArrayList<>(map.values()));
        }).when(mock).findAll();

        // #findById(String)
        Mockito.doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            return Optional.ofNullable(map.get(id));
        }).when(mock).findById(Mockito.anyString());

        // #existsById(String)
        Mockito.doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            return map.containsKey(id);
        }).when(mock).existsById(Mockito.anyString());

        // #save(Course)
        Mockito.doAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            map.put(course.getCode(), course);
            return course;
        }).when(mock).save(Mockito.any(Course.class));

        // #deleteById(String)
        Mockito.doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            map.remove(id);
            return null;
        }).when(mock).deleteById(Mockito.anyString());

        // Inject mock into controller
        Field field = controller.getClass().getDeclaredField("courseRepository");
        field.setAccessible(true);
        field.set(controller, mock);
    }

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
        ResponseEntity<Course> response = controller.getCourse(c0.getCode());

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
        ResponseEntity<Course> response = controller.getCourse(c0.getCode());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/courses/ response for an existing course.
     */
    @Test
    @Order(6)
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
    @Order(7)
    public void testRemove() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Course> response = controller.removeCourse(c0.getCode());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /course/remove/ response for a non existent course.
     */
    @Test
    @Order(8)
    public void testRemoveNonExisting() {
        ResponseEntity<Course> response = controller.removeCourse(c0.getCode());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
