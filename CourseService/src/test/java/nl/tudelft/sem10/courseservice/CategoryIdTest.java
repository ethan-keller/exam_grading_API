package nl.tudelft.sem10.courseservice;

import nl.tudelft.sem10.courseservice.entities.Category;
import nl.tudelft.sem10.courseservice.entities.CategoryId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CategoryId} objects.
 */
public class CategoryIdTest {
    private final transient String courseCode = "CSE9999";
    private final transient String categoryName = "MIDTERM";
    private final transient CategoryId c0 = new CategoryId(courseCode, categoryName);
    private final transient CategoryId c1 = new CategoryId(courseCode, categoryName);
    private final transient CategoryId c2 = new CategoryId("CSE0000", categoryName);
    private final transient Category nullCategoryId = null;

    /**
     * Test {@link CategoryId#getCourse()}.
     * Note that this class does not need field accessors
     * and that the fields cannot be transient because
     * they need to be serializable.
     * They just exist to keep PMD happy.
     * This is why no implementation is expected.
     */
    @Test
    public void testGetCourse() {
        Assertions.assertThrows(UnsupportedOperationException.class, c0::getCourse);
    }

    /**
     * Test {@link CategoryId#setCourse(String)}.
     * Note that this class does not need field accessors
     * and that the fields cannot be transient because
     * they need to be serializable.
     * They just exist to keep PMD happy.
     * This is why no implementation is expected.
     */
    @Test
    public void testSetCourse() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> c0.setCourse("NewCourseCode"));
    }

    /**
     * Test {@link CategoryId#getName()}.
     * Note that this class does not need field accessors
     * and that the fields cannot be transient because
     * they need to be serializable.
     * They just exist to keep PMD happy.
     * This is why no implementation is expected.
     */
    @Test
    public void testGetName() {
        Assertions.assertThrows(UnsupportedOperationException.class, c0::getName);
    }

    /**
     * Test {@link CategoryId#setName(String)}.
     * Note that this class does not need field accessors
     * and that the fields cannot be transient because
     * they need to be serializable.
     * They just exist to keep PMD happy.
     * This is why no implementation is expected.
     */
    @Test
    public void testSetName() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> c0.setName("NewCategoryName"));
    }

    /**
     * Test {@link CategoryId#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Assertions.assertEquals(960982880, c0.hashCode());
    }

    /**
     * Test {@link CategoryId#equals(Object)}.
     * This test compares the same instance.
     */
    @Test
    public void testEqualsSame() {
        Assertions.assertTrue(c0.equals(c0));
    }

    /**
     * Test {@link CategoryId#equals(Object)}.
     * This test compares different but equal instances.
     */
    @Test
    public void testEqualsEqual() {
        Assertions.assertTrue(c0.equals(c1));
    }

    /**
     * Test {@link CategoryId#equals(Object)}.
     * This test compares different and non-equal instances.
     */
    @Test
    public void testEqualsNotEqual() {
        Assertions.assertFalse(c0.equals(c2));
    }

    /**
     * Test {@link CategoryId#equals(Object)}.
     * This test compares an instance to null.
     */
    @Test
    public void testEqualsNull() {
        // Most IDEs give you a warning here because .equals(null) should always be false
        Assertions.assertFalse(c0.equals(nullCategoryId));
    }

    /**
     * Test {@link CategoryId#equals(Object)}.
     * This test compares an instance to an object of a different type.
     */
    @Test
    public void testEqualsDifferent() {
        Object myObject = "A string";
        Assertions.assertFalse(c0.equals(myObject));
    }

    /**
     * Test {@link CategoryId#toString()}.
     */
    @Test
    public void testToString() {
        Assertions.assertEquals("CategoryID[course=CSE9999, name=MIDTERM]", c0.toString());
    }
}
