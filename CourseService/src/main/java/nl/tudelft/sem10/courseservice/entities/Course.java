package nl.tudelft.sem10.courseservice.entities;

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
    @Column(name = "id")
    private long id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_code")
    private String code;

    /**
     * No-args constructor for internal use.
     */
    public Course() {
        // Nothing
    }
    /**
     * Create a course object with given ID, name and course code.
     *
     * @param id - long Course ID.
     * @param name - String Course name.
     * @param code - String Course code.
     */
    public Course(long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    // Getter setters

    /**
     * @return the course ID.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id - long New course ID.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the course name (may be null).
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - String New course name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the course code (may be null).
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code - String New course code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
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
        return id == course.id
                && Objects.equals(name, course.name)
                && Objects.equals(code, course.code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Course[id=").append(id).append(", name=").append(name).append(", code=").append(code).append("]").toString();
    }
}
