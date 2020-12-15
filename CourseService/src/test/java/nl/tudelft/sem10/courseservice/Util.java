package nl.tudelft.sem10.courseservice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A utility class to help with test creation.
 */
public class Util {
    private Util() {
        // Nothing
    }

    /**
     * Create a mocked JpaRepository.
     * This mocks:
     *  - #findAll()
     *  - #findById(V)
     *  - #existsById(V)
     *  - #save(U)
     *  - #deleteById(V)
     *
     * @param clazz - Class Repository type.
     * @param type - Class Repository content type.
     * @param identifier - Class Repository content identifier type.
     * @param idMapper - Mapping function to get an identifier from a type instance.
     * @param <T> - Repository type.
     * @param <U> - Content type.
     * @param <V> - Identifier type.
     * @return the mocked repository.
     */
    public static <T extends JpaRepository<U, V>, U, V> T repositoryMock(Class<T> clazz,
                                                                         Class<U> type,
                                                                         Class<V> identifier,
                                                                         Function<U, V> idMapper) {
        Map<V, U> map = new HashMap<>();
        T mock = Mockito.mock(clazz);

        // #findAll()
        Mockito.doAnswer(invocation -> {
            return Collections.unmodifiableList(new ArrayList<>(map.values()));
        }).when(mock).findAll();

        // #findById(V)
        Mockito.doAnswer(invocation -> {
            V id = invocation.getArgument(0, identifier);
            return Optional.ofNullable(map.get(id));
        }).when(mock).findById(Mockito.any(identifier));

        // #existsById(V)
        Mockito.doAnswer(invocation -> {
            V id = invocation.getArgument(0, identifier);
            return map.containsKey(id);
        }).when(mock).existsById(Mockito.any(identifier));

        // #save(U)
        Mockito.doAnswer(invocation -> {
            U u = invocation.getArgument(0, type);
            map.put(idMapper.apply(u), u);
            return u;
        }).when(mock).save(Mockito.any(type));

        // #deleteById(identifier)
        Mockito.doAnswer(invocation -> {
            V id = invocation.getArgument(0, identifier);
            map.remove(id);
            return null;
        }).when(mock).deleteById(Mockito.any(identifier));

        return mock;
    }

    /**
     * Get a field using reflection.
     * The field will be accessible immediately.
     *
     * @param clazz - Class&lt;?&gt; Class to get a field from.
     * @param fieldName - String Field name.
     * @return the field.
     * @throws ReflectiveOperationException If something goes wrong.
     */
    public static Field getField(Class<?> clazz,
                                 String fieldName) throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    /**
     * Set a field value using reflection.
     *
     * @param instance - Object Instance to set a field for.
     * @param fieldName - String Field name.
     * @param value - Object Value to set.
     * @throws ReflectiveOperationException If something goes wrong.
     */
    public static void setField(Object instance,
                                String fieldName,
                                Object value) throws ReflectiveOperationException {
        Field field = getField(instance.getClass(), fieldName);
        field.set(instance, value);
    }
}
