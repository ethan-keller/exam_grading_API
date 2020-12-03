package nl.tudelft.sem10.courseservice.repositories;

import nl.tudelft.sem10.courseservice.entities.Category;
import nl.tudelft.sem10.courseservice.entities.CategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository managing all course categories.
 */
public interface CategoryRepository extends JpaRepository<Category, CategoryId> {
    // Nothing
}
