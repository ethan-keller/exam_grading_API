package nl.tudelft.sem10.courseservice.domain.repository;

import java.io.Serializable;
import java.util.Objects;
import nl.tudelft.sem10.courseservice.domain.model.Category;

/**
 * A composite key for {@link Category} objects used by the repository.
 */
public class CategoryId implements Serializable {
    private static final long serialVersionUID = 7238060586464531770L;
    private String course; //NOPMD
    private String name; //NOPMD

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
     * @param name   - String Category name.
     */
    public CategoryId(String course, String name) {
        this.course = course;
        this.name = name;
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
