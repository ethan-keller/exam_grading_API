package nl.tudelft.sem10.courseservice.application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import nl.tudelft.sem10.courseservice.domain.model.Category;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryId;
import nl.tudelft.sem10.courseservice.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


/**
 * A category service implementation using a {@link CategoryRepository}.
 */
@Component
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
        Map<String, Double> map = new HashMap<>();

        // This is not the cleanest solution
        // TODO: Dedicated query to match by course code
        Iterator<Category> iterator = get().iterator();
        while (iterator.hasNext()) {
            Category category = iterator.next();
            if (category.getCourse().equals(course)) {

                // This should always be unique
                map.put(category.getName(), category.getWeight());
            }
        }

        return map;
    }

    @Override
    protected JpaRepository<Category, CategoryId> getRepository() {
        return categoryRepository;
    }
}
