package nl.tudelft.sem10.courseservice.application;

import nl.tudelft.sem10.courseservice.domain.model.Category;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryId;

import java.util.Map;

/**
 * A category service.
 */
public interface CategoryService extends RepositoryService<Category, CategoryId> {

    /**
     * Get all known weights for a course.
     *
     * @param course - String Course code.
     * @return the category/weight pairs.
     */
    public Map<String, Double> getWeights(String course);
}
