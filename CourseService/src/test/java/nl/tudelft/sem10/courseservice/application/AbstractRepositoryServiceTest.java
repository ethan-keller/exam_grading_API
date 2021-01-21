package nl.tudelft.sem10.courseservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem10.courseservice.CourseServiceApplication;
import nl.tudelft.sem10.courseservice.domain.model.Category;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryId;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = CourseServiceApplication.class)
class AbstractRepositoryServiceTest {
    @MockBean
    private transient CategoryRepository categoryRepository;

    @Autowired
    @Qualifier("getCategoryService")
    private transient CategoryService categoryService;

    @Test
    void testGet() {
        final List<Category> value = Collections.singletonList(new Category());
        when(categoryRepository.findAll())
            .thenReturn(value);
        final Iterable<Category> categories = categoryService.get();
        assertNotNull(categories);
        assertEquals(value, categories);
    }

    @Test
    void testRemoveId() {

        final Category data = new Category("CSE1", "memes", 0.6);
        final CategoryId id = new CategoryId("CSE1", "memes");

        when(categoryRepository.existsById(id)).thenReturn(true);
        when(categoryRepository.save(data)).thenReturn(data);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(data));

        categoryService.add(data);

        assertNotNull(categoryService.get(id));
        final var result = categoryService.remove(id);

        // To mimic CategoryRepository's actual behaviour
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        verify(categoryRepository, times(1)).deleteById(id);
        assertNull(categoryService.get(id));

        assertEquals(data, result);
    }


}