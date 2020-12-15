package nl.tudelft.sem10.courseservice.domain;

import nl.tudelft.sem10.courseservice.application.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository managing all course categories.
 */
public interface CategoryRepository extends JpaRepository<Category, CategoryId> {
    // Nothing
}
