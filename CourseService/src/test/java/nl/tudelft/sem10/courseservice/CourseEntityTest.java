package nl.tudelft.sem10.courseservice;

import nl.tudelft.sem10.courseservice.domain.model.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Course} entities.
 */
public class CourseEntityTest {
    private final transient String courseName = "TEST_COURSE";
    private final transient String courseCode = "CSE9999";
    private final transient Course c0 = new Course(courseCode, courseName);
    private final transient Course c1 = new Course(courseCode, courseName);
    private final transient Course c2 = new Course(courseCode, "TEST_COURSE_2");
    private final transient Course nullCourse = null;

    /**
     * Test {@link Course#getName()}.
     */
    @Test
    public void testGetName() {
        Assertions.assertEquals(courseName, c0.getName());
    }

    /**
     * Test {@link Course#setName(String)}.
     */
    @Test
    public void testSetName() {
        Course course = new Course(courseCode, courseName);
        course.setName("NewCourseName");

        // We assume the getter works as intended, as it is tested in a separate method.
        Assertions.assertEquals("NewCourseName", course.getName());
    }

    /**
     * Test {@link Course#getCode()}.
     */
    @Test
    public void testGetCode() {
        Assertions.assertEquals(courseCode, c0.getCode());
    }

    /**
     * Test {@link Course#setCode(String)}.
     */
    @Test
    public void testSetCode() {
        Course course = new Course(courseCode, courseName);
        course.setCode("NewCourseCode");

        // We assume the getter works as intended, as it is tested in a separate method.
        Assertions.assertEquals("NewCourseCode", course.getCode());
    }

    /**
     * Test {@link Course#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Assertions.assertEquals(-1027667660, c0.hashCode());
    }

    /**
     * Test {@link Course#equals(Object)}.
     * This test compares the same instance.
     */
    @Test
    public void testEqualsSame() {
        Assertions.assertTrue(c0.equals(c0));
    }

    /**
     * Test {@link Course#equals(Object)}.
     * This test compares different but equal instances.
     */
    @Test
    public void testEqualsEqual() {
        Assertions.assertTrue(c0.equals(c1));
    }

    /**
     * Test {@link Course#equals(Object)}.
     * This test compares different and non-equal instances.
     */
    @Test
    public void testEqualsNotEqual() {
        Assertions.assertFalse(c0.equals(c2));
    }

    /**
     * Test {@link Course#equals(Object)}.
     * This test compares an instance to null.
     */
    @Test
    public void testEqualsNull() {
        // Most IDEs give you a warning here because .equals(null) should always be false
        Assertions.assertFalse(c0.equals(nullCourse));
    }

    /**
     * Test {@link Course#equals(Object)}.
     * This test compares an instance to an object of a different type.
     */
    @Test
    public void testEqualsDifferent() {
        Object myObject = "A string";
        Assertions.assertFalse(c0.equals(myObject));
    }

    /**
     * Test {@link Course#toString()}.
     */
    @Test
    public void testToString() {
        Assertions.assertEquals("Course[code=CSE9999, name=TEST_COURSE]", c0.toString());
    }
}
