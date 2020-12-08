package nl.tudelft.sem10.courseservice;

import nl.tudelft.sem10.courseservice.entities.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Category} entities.
 */
public class CategoryEntityTest {
    private final transient String courseCode = "CSE9999";
    private final transient String categoryName = "MIDTERM";
    private final transient Category c0 = new Category(courseCode, categoryName, 0.5D);
    private final transient Category c1 = new Category(courseCode, categoryName, 0.5D);
    private final transient Category c2 = new Category("CSE0000", categoryName, 0.5D);
    private final transient Category nullCategory = null;

    /**
     * Test {@link Category#getCourse()}.
     */
    @Test
    public void testGetCourse() {
        Assertions.assertEquals(courseCode, c0.getCourse());
    }

    /**
     * Test {@link Category#setCourse(String)}.
     */
    @Test
    public void testSetCourse() {
        Category category = new Category(courseCode, categoryName, 0.5D);
        category.setCourse("NewCourseCode");

        // We assume the getter works as intended, as it is tested in a separate method.
        Assertions.assertEquals("NewCourseCode", category.getCourse());
    }

    /**
     * Test {@link Category#getName()}.
     */
    @Test
    public void testGetName() {
        Assertions.assertEquals(categoryName, c0.getName());
    }

    /**
     * Test {@link Category#setName(String)}.
     */
    @Test
    public void testSetName() {
        Category category = new Category(courseCode, categoryName, 0.5D);
        category.setName("NewCategoryName");

        // We assume the getter works as intended, as it is tested in a separate method.
        Assertions.assertEquals("NewCategoryName", category.getName());
    }

    /**
     * Test {@link Category#getWeight()}.
     */
    @Test
    public void testGetWeight() {
        Assertions.assertEquals(0.5D, c0.getWeight());
    }

    /**
     * Test {@link Category#setWeight(double)}.
     */
    @Test
    public void testSetWeight() {
        Category category = new Category(courseCode, categoryName, 0.5D);
        category.setWeight(1D);

        // We assume the getter works as intended, as it is tested in a separate method.
        Assertions.assertEquals(1D, category.getWeight());
    }

    /**
     * Test {@link Category#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Assertions.assertEquals(797342880, c0.hashCode());
    }

    /**
     * Test {@link Category#equals(Object)}.
     * This test compares the same instance.
     */
    @Test
    public void testEqualsSame() {
        Assertions.assertTrue(c0.equals(c0));
    }

    /**
     * Test {@link Category#equals(Object)}.
     * This test compares different but equal instances.
     */
    @Test
    public void testEqualsEqual() {
        Assertions.assertTrue(c0.equals(c1));
    }

    /**
     * Test {@link Category#equals(Object)}.
     * This test compares different and non-equal instances.
     */
    @Test
    public void testEqualsNotEqual() {
        Assertions.assertFalse(c0.equals(c2));
    }

    /**
     * Test {@link Category#equals(Object)}.
     * This test compares an instance to null.
     */
    @Test
    public void testEqualsNull() {
        // Most IDEs give you a warning here because .equals(null) should always be false
        Assertions.assertFalse(c0.equals(nullCategory));
    }

    /**
     * Test {@link Category#equals(Object)}.
     * This test compares an instance to an object of a different type.
     */
    @Test
    public void testEqualsDifferent() {
        Object myObject = "A string";
        Assertions.assertFalse(c0.equals(myObject));
    }

    /**
     * Test {@link Category#toString()}.
     */
    @Test
    public void testToString() {
        Assertions.assertEquals(
                "Category[course=CSE9999, name=MIDTERM, weight=0.5]",
                c0.toString());
    }
}
