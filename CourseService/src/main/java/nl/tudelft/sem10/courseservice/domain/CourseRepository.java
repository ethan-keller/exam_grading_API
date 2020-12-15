package nl.tudelft.sem10.courseservice.domain;

import nl.tudelft.sem10.courseservice.application.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository managing all courses.
 */
public interface CourseRepository extends JpaRepository<Course, String> {
    // Nothing
}
