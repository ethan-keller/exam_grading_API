package nl.tudelft.sem10.courseservice;

import nl.tudelft.sem10.courseservice.application.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link AuthService.UserType} enum.
 */
public class AuthServiceTest {
    /**
     * Test {@link AuthService.UserType#of(String)}.
     * Test if TEACHER is matched.
     */
    @Test
    public void testOfTeacher() {
        Assertions.assertEquals(AuthService.UserType.TEACHER, AuthService.UserType.of("TEACHER"));
    }

    /**
     * Test {@link AuthService.UserType#of(String)}.
     * Test if OTHER is matched for non-teacher types.
     */
    @Test
    public void testOfNonTeacher() {
        Assertions.assertEquals(AuthService.UserType.OTHER, AuthService.UserType.of("STUDENT"));
    }
}
