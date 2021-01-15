package nl.tudelft.sem10.courseservice.domain.repository;

import java.util.List;
import nl.tudelft.sem10.courseservice.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository managing all course categories.
 */
public interface CategoryRepository extends JpaRepository<Category, CategoryId> {

    // The table and column name must match the schema from Category
    @Query(value = "SELECT * FROM categories WHERE course_code = ?1", nativeQuery = true)
    public List<Category> getWeights(String course);
}
