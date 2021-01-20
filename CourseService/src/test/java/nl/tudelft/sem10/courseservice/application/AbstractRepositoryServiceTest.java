package nl.tudelft.sem10.courseservice.application;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Map;
import nl.tudelft.sem10.courseservice.CourseServiceApplication;
import nl.tudelft.sem10.courseservice.domain.model.Category;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(classes = CourseServiceApplication.class)
class AbstractRepositoryServiceTest {

    @Autowired
    @Qualifier("getCategoryService")
    CategoryService categoryService;

    // This only works on Adriaan's production database
    @Disabled
    @Test
    void testGet() {
        final Iterable<Category> categories = categoryService.get();
        assertNotNull(categories);
    }

    // This only works on Adriaan's production database
    @Disabled
    @Test
    void testRemoveId() {
        final Category data = new Category("CSE1", "memes", 0.6);
        categoryService.add(data);
        assertNotNull(categoryService.get(new CategoryId("CSE1", "memes")));

        var result = categoryService.remove(new CategoryId("CSE1", "memes"));

        assertNull(categoryService.get(new CategoryId("CSE1", "memes")));
        assertEquals(data, result);
    }


}