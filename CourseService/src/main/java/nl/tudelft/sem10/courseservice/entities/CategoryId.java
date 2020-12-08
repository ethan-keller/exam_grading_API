package nl.tudelft.sem10.courseservice.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * A composite key for {@link Category} objects used by the repository.
 */
public class CategoryId implements Serializable {
    private static final long serialVersionUID = 7238060586464531770L;
    private String course;
    private String name;

    /**
     * No-args constructor for internal use.
     */
    public CategoryId() {
        // Nothing
    }

    /**
     * Create a category ID object with given course code and name.
     *
     * @param course - String Course code
     * @param name - String Category name.
     */
    public CategoryId(String course, String name) {
        this.course = course;
        this.name = name;
    }

    /**
     * Get the course code.
     *
     * @return the course code.
     */
    public String getCourse() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the course code.
     *
     * @param course - String Course code.
     */
    public void setCourse(String course) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the category name.
     *
     * @return the category name.
     */
    public String getName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the category name.
     *
     * @param name - String category name.
     * @return the category name.
     */
    public String setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, name);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CategoryId)) {
            return false;
        }
        CategoryId other = (CategoryId) object;
        return Objects.equals(course, other.course)
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("CategoryID[course=")
                .append(course)
                .append(", name=")
                .append(name)
                .append("]")
                .toString();
    }
}