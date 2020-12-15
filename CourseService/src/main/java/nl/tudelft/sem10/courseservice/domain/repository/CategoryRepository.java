package nl.tudelft.sem10.courseservice.domain.repository;

import nl.tudelft.sem10.courseservice.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository managing all course categories.
 */
public interface CategoryRepository extends JpaRepository<Category, CategoryId> {
    // Nothing
}
