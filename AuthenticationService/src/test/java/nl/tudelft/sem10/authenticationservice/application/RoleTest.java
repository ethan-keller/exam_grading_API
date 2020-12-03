package nl.tudelft.sem10.authenticationservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

class RoleTest {

    private final Role role1 = new Role(RoleType.STUDENT);
    private final Role role2 = new Role(RoleType.TEACHER);
    private final Role role3 = new Role(RoleType.STUDENT);

    @Test
    void getName() {
        assertEquals("TEACHER", role2.getName());
        assertEquals("STUDENT", role1.getName());
    }

    @Test
    void getType() {
        assertEquals(RoleType.STUDENT, role1.getType());
        assertEquals(RoleType.TEACHER, role2.getType());;
    }

    @Test
    void testEquals() {
        assertEquals(role3, role1);
        assertNotEquals(role1, role2);
    }

    @Test
    void testHashCode() {
        assertEquals(role1.hashCode(), role3.hashCode());
        assertNotEquals(role1.hashCode(), role2.hashCode());
    }
}
