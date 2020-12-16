package nl.tudelft.sem10.courseservice.application;

import java.util.Objects;
import java.util.function.Function;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A shell {@link RepositoryService} implementation using a {@link JpaRepository}.
 *
 * @param <T> - Data type.
 * @param <U> - Data identifier type.
 */
public abstract class AbstractRepositoryService<T, U> implements RepositoryService<T, U> {
    private final transient Function<T, U> idMapper;

    /**
     * Create a repository service.
     *
     * @param idMapper - Function&lt;T, U&gt; Mapping function to get a key from a data entity.
     */
    protected AbstractRepositoryService(Function<T, U> idMapper) {
        this.idMapper = Objects.requireNonNull(idMapper);
    }

    @Override
    public Iterable<T> get() {
        return getRepository().findAll();
    }

    @Override
    public T get(U id) {
        // Get an entry by ID or null if no such entry exists
        return getRepository().findById(id).orElse(null);
    }

    @Override
    public T add(T data) {
        // Duplicate entry
        if (getRepository().existsById(idMapper.apply(data))) {
            return null;
        }

        // Save the entry
        getRepository().save(data);

        return data;
    }

    @Override
    public T remove(U id) {
        T t = getRepository().findById(id).orElse(null);

        // No such entry
        if (t == null) {
            return null;
        }

        // Delete the entry
        getRepository().deleteById(id);

        // Return the deleted entry
        return t;
    }

    /**
     * Get the backing repository.
     *
     * @return the backing repository.
     */
    protected abstract JpaRepository<T, U> getRepository();
}
