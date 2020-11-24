package nl.tudelft.sem10.courseservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

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

    public Course(long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                Objects.equals(name, course.name) &&
                Objects.equals(code, course.code);
    }

}
