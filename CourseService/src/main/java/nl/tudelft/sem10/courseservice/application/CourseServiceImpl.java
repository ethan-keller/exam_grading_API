package nl.tudelft.sem10.courseservice.application;

import nl.tudelft.sem10.courseservice.domain.model.Course;
import nl.tudelft.sem10.courseservice.domain.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * A course service implementation using a {@link CourseRepository}.
 */
@Component("CourseServiceImpl")
public class CourseServiceImpl extends AbstractRepositoryService<Course, String>
    implements CourseService {
    @Autowired
    private CourseRepository courseRepository; //NOPMD

    /**
     * No-args constructor for internal use.
     */
    public CourseServiceImpl() {
        super(Course::getCode);
    }

    @Override
    protected JpaRepository<Course, String> getRepository() {
        return courseRepository;
    }
}
