package nl.tudelft.sem10.courseservice;

import java.util.Iterator;
import nl.tudelft.sem10.courseservice.application.CategoryServiceImpl;
import nl.tudelft.sem10.courseservice.domain.model.Category;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryId;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryRepository;
import nl.tudelft.sem10.courseservice.framework.CategoryController;
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
 * Note that these tests assume the implementation {@link CategoryServiceImpl} is used.
 */
@SpringBootTest(classes = CourseServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceApplicationTests {
    private final transient Category c0 = new Category("CSE9999", "MIDTERM", 0.5D);
    private final transient String validToken = "MyTeacherToken";

    @Autowired
    private transient CategoryController controller;

    /**
     * Mock the repository so no database connection is required.
     * We assume the controller and its service are autowired correctly.
     */
    @BeforeAll
    public void setup() throws ReflectiveOperationException {
        Object service = Util.getField(CategoryController.class, "categoryService").get(controller);

        // Only for the CourseService implementation do we inject a mock repository
        if (!(service instanceof CategoryServiceImpl)) {
            return;
        }

        // Inject mock into controller
        // Yes this is really ugly but at least PMD will not complain
        Util.setField(service,
                "categoryRepository",
                Util.repositoryMock(
                        CategoryRepository.class,
                        Category.class,
                        CategoryId.class,
                        category -> {
                            return new CategoryId(category.getCourse(), category.getName());
                        }));

        // Inject auth mock
        Util.setField(controller, "auth", Util.mockAuth());
    }

    /**
     * Test /category/get/ response for a non existent category.
     */
    @Test
    @Order(1)
    public void testGetNonExistent() {
        ResponseEntity<Category> response = controller.getCategory(c0.getCourse(), c0.getName());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test /category/add/ response adding a new category.
     */
    @Test
    @Order(2)
    public void testAdd() {
        // Note that we assume no such category exists,
        // as the test will delete the test category too
        ResponseEntity<Category> response = controller.addCategory(validToken, c0);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /category/add/ response adding a category with duplicate ID.
     */
    @Test
    @Order(3)
    public void testAddDuplicate() {
        ResponseEntity<Category> response = controller.addCategory(validToken, c0);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Test /category/get/ response for an existing category.
     */
    @Test
    @Order(4)
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
    @Order(5)
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
    @Order(6)
    public void testRemove() {
        // Note that this entity should exist, as we inserted it in a previous test
        ResponseEntity<Category> response = controller.removeCategory(validToken,
                c0.getCourse(),
                c0.getName());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(c0, response.getBody());
    }

    /**
     * Test /category/remove/ response for a non existent category.
     */
    @Test
    @Order(7)
    public void testRemoveNonExisting() {
        ResponseEntity<Category> response = controller.removeCategory(validToken,
                c0.getCourse(),
                c0.getName());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test /category/add/ response for a non-teacher user.
     */
    @Test
    @Order(8)
    public void testAddWrongUser() {
        ResponseEntity<Category> response = controller.addCategory("MyNonTeacherToken", c0);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test /category/add/ response for an unknown user.
     */
    @Test
    @Order(9)
    public void testAddInvalidUser() {
        ResponseEntity<Category> response = controller.addCategory("MyInvalidToken", c0);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Test /category/remove/ response for a non-teacher user.
     */
    @Test
    @Order(10)
    public void testRemoveWrongUser() {
        ResponseEntity<Category> response = controller.removeCategory("MyNonTeacherToken",
                "CSE9999",
                "MIDTERM");

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test /category/remove/ response for an unknown user.
     */
    @Test
    @Order(11)
    public void testRemoveInvalidUser() {
        ResponseEntity<Category> response = controller.removeCategory("MyInvalidToken",
                "CSE9999",
                "MIDTERM");

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}