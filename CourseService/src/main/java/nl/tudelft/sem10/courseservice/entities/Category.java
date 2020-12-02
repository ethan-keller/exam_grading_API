package nl.tudelft.sem10.courseservice.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * A course category object.
 */
@Entity
@Table(name = "categories")
@IdClass(CategoryId.class)
public class Category {

    @Id
    @Column(name = "course_code")
    private String course;

    @Id
    @Column(name = "category_name")
    private String name;

    @Column(name = "category_weight", nullable = false)
    private double weight;

    /**
     * No-args constructor for internal use.
     */
    public Category() {
        // Nothing
    }

    /**
     * Create a category object with given course code, name and weight.
     *
     * @param course - String Course code
     * @param name - String Category name.
     * @param weight - double Category weight.
     */
    public Category(String course, String name, double weight) {
        this.course = course;
        this.name = name;
        this.weight = weight;
    }

    /**
     * Get the current course code.
     *
     * @return the course code (may be null).
     */
    public String getCourse() {
        return course;
    }

    /**
     * Set a new course code.
     *
     * @param course - String New course code.
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Get the current category name.
     *
     * @return the category name (may be null).
     */
    public String getName() {
        return name;
    }

    /**
     * Set a new category name.
     *
     * @param name - String New category name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the current category weight.
     *
     * @return the category weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Set a new category weight.
     *
     * @param weight - double New category weight.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, name, weight);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        return Objects.equals(course, other.course)
                && Objects.equals(name, other.name)
                && Objects.equals(weight, other.weight);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Category[course=")
                .append(course)
                .append(", name=")
                .append(name)
                .append(", weight=")
                .append(weight)
                .append("]")
                .toString();
    }
}
