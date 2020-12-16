package nl.tudelft.sem10.courseservice.application;

import nl.tudelft.sem10.courseservice.domain.model.Course;

/**
 * A service for interacting with data.
 *
 * @param <T> Data type.
 * @param <U> Data identifier type.
 */
public interface RepositoryService<T, U> {
    /**
     * Get all known data entries.
     *
     * @return a collection of all entries.
     */
    public Iterable<T> get();

    /**
     * Get a data entry by ID.
     *
     * @param id - U Identifier.
     * @return the data entry or null if it does not exist.
     */
    public T get(U id);

    /**
     * Add a data entry.
     *
     * @param data - T Entry to add.
     * @return the added entry or null if no entry was added.
     */
    public T add(T data);

    /**
     * Remove a data entry.
     *
     * @param id - U Identifier.
     * @return the removed entry or null if no entry was removed.
     */
    public T remove(U id);
}
