package nl.tudelft.sem10.authenticationservice.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test suite for the User class.
 */
class UserTest {

    private final transient User user1 = new User("jSnow", "pass123$", 0);
    private final transient User user2 = new User("aBlack", "pass456!", 0);
    private final transient User user3 = new User("dBlue", "pass789#", 1);
    private final transient User user4 = new User("aBlack", "pass456!", 0);
    private final transient User user5 = new User("aBlack", "blablabla", 1);

    /**
     * Test for netId getter.
     */
    @Test
    void getNetId() {
        assertEquals("jSnow", user1.getNetId());
    }

    /**
     * Test for password getter.
     */
    @Test
    void getPassword() {
        assertEquals("pass123$", user1.getPassword());
    }

    /**
     * Test for role getter.
     */
    @Test
    void getRole() {
        assertEquals(new Role(RoleType.STUDENT), user1.getRole());
        assertEquals(new Role(RoleType.TEACHER), user3.getRole());
    }

    /**
     * Test for integer role type getter.
     */
    @Test
    void getRoleInteger() {
        assertEquals(0, user1.getRoleInteger());
        assertEquals(1, user3.getRoleInteger());
        User u = new User("netId", "pass", -1);
        u.getRole().setType(null);
        assertEquals(-1, u.getRoleInteger());
    }

    /**
     * Test for equals method.
     */
    @Test
    void testEquals() {
        assertEquals(user2, user5);
        assertNotEquals(user2, user3);
        assertEquals(user2, user2);
        assertNotEquals(user1, new Object());
    }
}
