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

    @Id
    @Column(name = "course_code")
    private String code;

    @Column(name = "course_name")
    private String name;

    /**
     * No-args constructor for internal use.
     */
    public Course() {
        // Nothing
    }

    /**
     * Create a course object with given name and course code.
     *
     * @param code - String Course code.
     * @param name - String Course name.
     */
    public Course(String code, String name) {
        this.code = code;
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

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
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
        return Objects.equals(code, course.code)
                && Objects.equals(name, course.name);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Course[code=")
                .append(code)
                .append(", name=")
                .append(name)
                .append("]")
                .toString();
    }
}
