package nl.tudelft.sem10.gradingservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "grade")
@SuppressWarnings({"PMD", "unused"})
public class Grade {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "mark")
    private float mark;

    @Column(name = "netid")
    private String netid;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "grade_type")
    private String gradeType;

    public Grade() {

    }

    /**
     *TODO: Insert javadoc here.
     *
     * @param id - long
     * @param netid - String
     * @param courseCode - String
     * @param gradeType - String
     * @param mark - float
     */
    public Grade(long id, float mark, String netid, String courseCode, String gradeType) {
        this.id = id;
        this.mark = mark;
        this.netid = netid;
        this.courseCode = courseCode;
        this.gradeType = gradeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return id == grade.id &&
                Float.compare(grade.mark, mark) == 0 &&
                Objects.equals(netid, grade.netid) &&
                Objects.equals(courseCode, grade.courseCode);
    }

    public long getId() {
        return id;
    }

    public float getMark() {
        return mark;
    }

    public String getNetid() {
        return netid;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getGradeType() {
        return gradeType;
    }
}
