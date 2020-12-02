package nl.tudelft.sem10.courseservice.controllers;

import nl.tudelft.sem10.courseservice.entities.Category;
import nl.tudelft.sem10.courseservice.entities.CategoryID;
import nl.tudelft.sem10.courseservice.entities.Course;
import nl.tudelft.sem10.courseservice.repositories.CategoryRepository;
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
 * Course category REST API.
 */
@Controller
@RequestMapping(path = "/category")
public class CategoryController {
    private static final String RESPONSE_TYPE = "application/json";

    @Autowired
    private CategoryRepository categoryRepository; //NOPMD

    /**
     * Get all available course categories.
     *
     * @return all categories.
     */
    @GetMapping(path = "/categories", produces = RESPONSE_TYPE)
    public ResponseEntity<Iterable<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Get a course category from the repository by course and name.
     *
     * @param courseCode - String Course code.
     * @param categoryName - String Category name.
     * @return the category or a 404 error if no such category exists.
     */
    @GetMapping(path = "/get", produces = RESPONSE_TYPE)
    public ResponseEntity<Category> getCategory(@RequestParam String courseCode, @RequestParam String categoryName) {

        // Get a course category by course code and name or null if no such category exists
        Category category = categoryRepository.findById(new CategoryID(courseCode, categoryName)).orElse(null);

        // Return the category if it exists or a 404 error
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * Put a new course category into the repository.
     *
     * @param category - Category Course category to add.
     * @return the added category or a 409 error if a category with the same course code and name already exists.
     */
    @PostMapping(path = "/add", produces = RESPONSE_TYPE)
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {

        // Duplicate category
        if (categoryRepository.existsById(new CategoryID(category.getCourse(), category.getName()))) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Save the category
        categoryRepository.save(category);

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * Remove a course category from the repository.
     *
     * @param courseCode - String Course code.
     * @param categoryName - String Category name.
     * @return the deleted course or a 204 error.
     */
    @DeleteMapping(path = "/remove", produces = RESPONSE_TYPE)
    public ResponseEntity<Category> removeCategory(@RequestParam String courseCode, @RequestParam String categoryName) {
        CategoryID id = new CategoryID(courseCode, categoryName);
        Category category = categoryRepository.findById(id).orElse(null);

        // No such category
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Delete the category
        categoryRepository.deleteById(id);

        // Return the deleted category
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
