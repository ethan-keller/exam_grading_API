package nl.tudelft.sem10.gradingservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD")
public class GradeTest {

    private transient Grade test;
    private transient Grade test2;
    private transient Grade test3;
    private transient Grade test4;
    private transient Grade test5;
    private transient Grade test6;

    @BeforeEach
    void setup() {
        test = new Grade(12, 10.0f, "Silverhand", "CSE2077", "Samurai");
        test2 = new Grade(11, 10.0f, "Silverhand", "CSE2077", "Samurai");
        test3 = new Grade(12, 9.0f, "Silverhand", "CSE2077", "Samurai");
        test4 = new Grade(12, 10.0f, "V", "CSE2077", "Samurai");
        test5 = new Grade(12, 10.0f, "Silverhand", "CSE2020", "Samurai");
        test6 = new Grade(12, 10.0f, "Silverhand", "CSE2077", "Soldier");
    }

    @Test
    void constructorTest() {
        assertNotNull(test);
    }

    @Test
    void defaultConstructorTest() {
        Grade test = new Grade();
        assertNotNull(test);
        assertNull(test.getCourseCode());
        assertNull(test.getGradeType());
        assertEquals(0, test.getId());
        assertEquals(0, test.getMark());
        assertNull(test.getNetid());
    }

    @Test
    void equalsTest() {
        assertTrue(test.equals(test));
        assertFalse(test.equals(null));
        assertFalse(test.equals("Im a string!"));
        assertFalse(test.equals(test2));
        assertFalse(test.equals(test3));
        assertFalse(test.equals(test4));
        assertFalse(test.equals(test5));
        assertFalse(test.equals(test6));
    }

    @Test
    void getIdTest() {
        assertEquals(12, test.getId());
    }

    @Test
    void getMarkTest() {
        assertEquals(10.0f, test.getMark());
    }

    @Test
    void getNetidTest() {
        assertEquals("Silverhand", test.getNetid());
    }

    @Test
    void getCourseCodeTest() {
        assertEquals("CSE2077", test.getCourseCode());
    }

    @Test
    void getGradeTypeTest() {
        assertEquals("Samurai", test.getGradeType());
    }
}
