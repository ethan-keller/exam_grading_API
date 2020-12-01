package nl.tudelft.sem10.courseservice.repositories;

import nl.tudelft.sem10.courseservice.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository managing all courses.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Nothing
}
