package nl.tudelft.sem10.authenticationservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Test suite for the Role class.
 */
class RoleTest {

    private final transient Role role1 = new Role(RoleType.STUDENT);
    private final transient Role role2 = new Role(RoleType.TEACHER);
    private final transient Role role3 = new Role(RoleType.STUDENT);

    /**
     * Test for name getter.
     */
    @Test
    void getName() {
        assertEquals("TEACHER", role2.getName());
        assertEquals("STUDENT", role1.getName());
        assertEquals("UNKNOWN", new Role(null).getName());
    }

    /**
     * Test for type getter.
     */
    @Test
    void getType() {
        assertEquals(RoleType.STUDENT, role1.getType());
        assertEquals(RoleType.TEACHER, role2.getType());
    }

    /**
     * Test for equals method.
     */
    @Test
    void testEquals() {
        assertEquals(role3, role1);
        assertNotEquals(role1, role2);
        assertEquals(role3, role3);
        assertNotEquals(role2, new Object());
    }

    /**
     * Test for hash code generator.
     */
    @Test
    void testHashCode() {
        assertEquals(role1.hashCode(), role3.hashCode());
        assertNotEquals(role1.hashCode(), role2.hashCode());
    }
}
