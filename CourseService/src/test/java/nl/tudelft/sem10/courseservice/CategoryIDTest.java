package nl.tudelft.sem10.courseservice;

import nl.tudelft.sem10.courseservice.entities.Category;
import nl.tudelft.sem10.courseservice.entities.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CategoryID} objects.
 */
public class CategoryIDTest {
    private final transient String courseCode = "CSE9999";
    private final transient String categoryName = "MIDTERM";
    private final transient CategoryID c0 = new CategoryID(courseCode, categoryName);
    private final transient CategoryID c1 = new CategoryID(courseCode, categoryName);
    private final transient CategoryID c2 = new CategoryID("CSE0000", categoryName);
    private final transient Category nullCategoryId = null;

    /**
     * Test {@link CategoryID#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Assertions.assertEquals(960982880, c0.hashCode());
    }

    /**
     * Test {@link CategoryID#equals(Object)}.
     * This test compares the same instance.
     */
    @Test
    public void testEqualsSame() {
        Assertions.assertTrue(c0.equals(c0));
    }

    /**
     * Test {@link CategoryID#equals(Object)}.
     * This test compares different but equal instances.
     */
    @Test
    public void testEqualsEqual() {
        Assertions.assertTrue(c0.equals(c1));
    }

    /**
     * Test {@link CategoryID#equals(Object)}.
     * This test compares different and non-equal instances.
     */
    @Test
    public void testEqualsNotEqual() {
        Assertions.assertFalse(c0.equals(c2));
    }

    /**
     * Test {@link CategoryID#equals(Object)}.
     * This test compares an instance to null.
     */
    @Test
    public void testEqualsNull() {
        // Most IDEs give you a warning here because .equals(null) should always be false
        Assertions.assertFalse(c0.equals(nullCategoryId));
    }

    /**
     * Test {@link CategoryID#equals(Object)}.
     * This test compares an instance to an object of a different type.
     */
    @Test
    public void testEqualsDifferent() {
        Object myObject = "A string";
        Assertions.assertFalse(c0.equals(myObject));
    }

    /**
     * Test {@link CategoryID#toString()}.
     */
    @Test
    public void testToString() {
        Assertions.assertEquals("CategoryID[course=CSE9999, name=MIDTERM]", c0.toString());
    }
}
