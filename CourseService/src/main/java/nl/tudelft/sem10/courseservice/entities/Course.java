package nl.tudelft.sem10.courseservice.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Course() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    /**
     *TODO: Insert javadoc here.
     *
     * @param id - long
     * @param name - String
     * @param code - String
     */
    public Course(long id, String name, String code) {
        this.id = id;
        this.name = name;
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

}
