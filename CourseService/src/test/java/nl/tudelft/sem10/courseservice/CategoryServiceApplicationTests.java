package nl.tudelft.sem10.courseservice;

import java.util.Iterator;
import nl.tudelft.sem10.courseservice.controllers.CategoryController;
import nl.tudelft.sem10.courseservice.entities.Category;
import nl.tudelft.sem10.courseservice.entities.CategoryID;
import nl.tudelft.sem10.courseservice.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests for all {@link CategoryController} endpoints.
 */
@SpringBootTest(classes = CourseServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceApplicationTests {
    private final transient Category c0 = new Category("CSE9999", "MIDTERM", 0.5D);

    @Autowired
    private transient CategoryController controller;

    /**
     * Mock the repository so no database connection is required.
     */
    @BeforeAll
    public void setup() throws ReflectiveOperationException {
        // Create repository mock
       CategoryRepository mock = Util.createMapBackedRepositoryMock(CategoryRepository.class, Category.class, CategoryID.class, category -> {
           return new CategoryID(category.getCourse(), category.getName());
       });

        // Inject mock into controller
        Util.setField(controller, "categoryRepository", mock);
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
     * Test /category/get/ response for a non existent category.
     */
    @Test
    @Order(2)
    public void testGetNonExistent() {
        ResponseEntity<Category> response = controller.getCategory(c0.getCourse(), c0.getName());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test /category/add/ response adding a new category.
     */
    @Test
    @Order(3)
    public void testAdd() {
        // Note that we assume no such category exists, as the test will delete the test category too
        ResponseEntity<Category> response = controller.addCategory(c0);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /category/add/ response adding a category with duplicate ID.
     */
    @Test
    @Order(4)
    public void testAddDuplicate() {
        ResponseEntity<Category> response = controller.addCategory(c0);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Test /category/get/ response for an existing category.
     */
    @Test
    @Order(5)
    public void testGet() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Category> response = controller.getCategory(c0.getCourse(), c0.getName());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /category/categories/ response for an existing category.
     */
    @Test
    @Order(6)
    public void testGetAll() {
        // Note that only one entity (c0) should exist, as we inserted it in a previous test
        ResponseEntity<Iterable<Category>> response = controller.getAllCategories();

        Iterable<Category> iterable = response.getBody();

        // Response has a body
        Assertions.assertNotNull(iterable);

        // This should not be null by contract
        Iterator<Category> iterator = iterable.iterator();

        // Category c0
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(c0, iterator.next());

        // Only one element so there should not be a next one
        Assertions.assertFalse(iterator.hasNext());
    }

    /**
     * Test /category/remove/ response for an existing category.
     */
    @Test
    @Order(7)
    public void testRemove() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Category> response = controller.removeCategory(c0.getCourse(), c0.getName());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /category/remove/ response for a non existent category.
     */
    @Test
    @Order(8)
    public void testRemoveNonExisting() {
        ResponseEntity<Category> response = controller.removeCategory(c0.getCourse(), c0.getName());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
