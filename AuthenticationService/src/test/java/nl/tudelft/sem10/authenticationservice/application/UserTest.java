package nl.tudelft.sem10.authenticationservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class UserTest {

    private final transient User user1 = new User("jSnow", "pass123$", 0);
    private final transient User user2 = new User("aBlack", "pass456!", 0);
    private final transient User user3 = new User("dBlue", "pass789#", 1);
    private final transient User user4 = new User("aBlack", "pass456!", 0);
    private final transient User user5 = new User("aBlack", "blablabla", 1);

    @Test
    void getNetId() {
        assertEquals("jSnow", user1.getNetId());
    }

    @Test
    void getPassword() {
        assertEquals("pass123$", user1.getPassword());
    }

    @Test
    void getRole() {
        assertEquals(new Role(RoleType.STUDENT), user1.getRole());
        assertEquals(new Role(RoleType.TEACHER), user3.getRole());
    }

    @Test
    void getRoleInteger() {
        assertEquals(0, user1.getRoleInteger());
        assertEquals(1, user3.getRoleInteger());
    }

    @Test
    void testEquals() {
        assertEquals(user2, user5);
        assertNotEquals(user2, user3);
        assertEquals(user2, user2);
        assertNotEquals(user1, new Object());
    }

    @Test
    void testHashCode() {
        assertEquals(user2.hashCode(), user4.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}
