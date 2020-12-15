package nl.tudelft.sem10.courseservice.framework;

import nl.tudelft.sem10.courseservice.application.AuthService;
import nl.tudelft.sem10.courseservice.application.CategoryService;
import nl.tudelft.sem10.courseservice.domain.model.Category;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryId;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Course category REST API.
 */
@Controller
@RequestMapping(path = "/teacher/category")
public class CategoryController {
    private static final String RESPONSE_TYPE = "application/json";

    @Autowired
    private AuthService auth; //NOPMD

    @Autowired
    private CategoryService categoryService; //NOPMD

    /**
     * Get all available course categories.
     *
     * @return all categories.
     */
    @GetMapping(path = "/categories", produces = RESPONSE_TYPE)
    public ResponseEntity<Iterable<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.get(), HttpStatus.OK);
    }

    /**
     * Get a course category from the repository by course and name.
     *
     * @param courseCode - String Course code.
     * @param categoryName - String Category name.
     * @return the category or a 404 error if no such category exists.
     */
    @GetMapping(path = "/get", produces = RESPONSE_TYPE)
    public ResponseEntity<Category> getCategory(@RequestParam String courseCode,
                                                @RequestParam String categoryName) {

        Category category = categoryService.get(new CategoryId(courseCode, categoryName));

        // The resource does not exist
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * Put a new course category into the repository.
     *
     * @param category - Category Course category to add.
     * @return the added category or a 409 error if a category
     *         with the same course code and name already exists.
     */
    @PostMapping(path = "/add", produces = RESPONSE_TYPE)
    public ResponseEntity<Category> addCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                                @RequestBody Category category) {
        // Auth
        ResponseEntity<Category> authRes = authResponse(authorization);
        if (authRes != null) {
            return authRes;
        }

        Category result = categoryService.add(category);

        // The resource already exists
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Remove a course category from the repository.
     *
     * @param courseCode - String Course code.
     * @param categoryName - String Category name.
     * @return the deleted course or a 204 error.
     */
    @DeleteMapping(path = "/remove", produces = RESPONSE_TYPE)
    public ResponseEntity<Category> removeCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                                   @RequestParam String courseCode,
                                                   @RequestParam String categoryName) {
        // Auth
        ResponseEntity<Category> authRes = authResponse(authorization);
        if (authRes != null) {
            return authRes;
        }

        Category result = categoryService.remove(new CategoryId(courseCode, categoryName));

        // The resource does not exist
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get a response entity for the authentication.
     * Note that this method only accepts {@link AuthService.UserType#TEACHER} users.
     *
     * @param token - String authorization token.
     * @param <T> - Entity type.
     * @return the response or null if the token is valid.
     */
    private <T> ResponseEntity<T> authResponse(String token) {
        AuthService.UserType type = auth.getUser(token);
        switch (type) {
            case TEACHER:
                return null;
            case UNKNOWN:
                // TODO: Return WWW-Authenticate header as required by RFC 7235
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            default:
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }
    }
}
