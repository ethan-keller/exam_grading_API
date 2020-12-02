package nl.tudelft.sem10.courseservice.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * A composite key for {@link Category} objects used by the repository.
 */
public class CategoryID implements Serializable {
    private String course;
    private String name;

    /**
     * No-args constructor for internal use.
     */
    public CategoryID() {
        // Nothing
    }

    /**
     * Create a category ID object with given course code and name.
     *
     * @param course - String Course code
     * @param name - String Category name.
     */
    public CategoryID(String course, String name) {
        this.course = course;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, name);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CategoryID)) {
            return false;
        }
        CategoryID other = (CategoryID) object;
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
