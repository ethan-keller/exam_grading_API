package nl.tudelft.sem10.courseservice.application;

import java.util.Map;
import java.util.stream.Collectors;
import nl.tudelft.sem10.courseservice.domain.model.Category;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryId;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * A category service implementation using a {@link CategoryRepository}.
 */
@Component("CategoryServiceImpl")
public class CategoryServiceImpl extends AbstractRepositoryService<Category, CategoryId>
        implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository; //NOPMD

    /**
     * No-args constructor for internal use.
     */
    public CategoryServiceImpl() {
        super(category -> new CategoryId(category.getCourse(), category.getName()));
    }

    @Override
    public Map<String, Double> getWeights(String course) {
        return categoryRepository.getWeights(course)
                .stream()
                .collect(Collectors.toMap(Category::getName, Category::getWeight));
    }

    @Override
    protected JpaRepository<Category, CategoryId> getRepository() {
        return categoryRepository;
    }
}
