package nl.tudelft.sem10.courseservice.application;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A course object.
 */
@Entity
@Table(name = "course")
public class Course {

    @Column(name = "course_name")
    private String name;

    @Id
    @Column(name = "course_code")
    private String code;

    /**
     * No-args constructor for internal use.
     */
    public Course() {
        // Nothing
    }

    /**
     * Create a course object with given name and course code.
     *
     * @param name - String Course name.
     * @param code - String Course code.
     */
    public Course(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * Get the current course name.
     *
     * @return the course name (may be null).
     */
    public String getName() {
        return name;
    }

    /**
     * Set a new course name.
     *
     * @param name - String New course name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the current course code.
     *
     * @return the course code (may be null).
     */
    public String getCode() {
        return code;
    }

    /**
     * Set a new course code.
     *
     * @param code - String New course code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(name, course.name)
                && Objects.equals(code, course.code);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Course[name=")
                .append(name)
                .append(", code=")
                .append(code)
                .append("]")
                .toString();
    }
}
