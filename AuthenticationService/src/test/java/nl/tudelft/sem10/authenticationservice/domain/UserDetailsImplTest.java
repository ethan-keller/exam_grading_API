package nl.tudelft.sem10.authenticationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem10.authenticationservice.application.Role;
import nl.tudelft.sem10.authenticationservice.application.RoleType;
import nl.tudelft.sem10.authenticationservice.application.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Test suite for the user details implementation class.
 */
class UserDetailsImplTest {

    private final transient User u1 = new User("jSnow", "pass123#", 0);
    private final transient User u2 = new User("aBeller", "pass456!", 1);
    private transient UserDetailsImpl userDetails1;
    private transient UserDetailsImpl userDetails2;

    /**
     * Test set up.
     */
    @BeforeEach
    void setUp() {
        userDetails1 = new UserDetailsImpl(u1);
        userDetails2 = new UserDetailsImpl(u2);
    }

    /**
     * Checks if student authority is correctly set and returned.
     */
    @Test
    void getStudentAuthorities() {
        SimpleGrantedAuthority role =
                new SimpleGrantedAuthority(new Role(RoleType.STUDENT).getName());
        assertTrue(userDetails1.getAuthorities().contains(role));
        assertEquals(1, userDetails1.getAuthorities().size());
    }

    /**
     * Checks if teacher authority is correctly set and returned.
     */
    @Test
    void getTeacherAuthorities() {
        SimpleGrantedAuthority role =
                new SimpleGrantedAuthority(new Role(RoleType.TEACHER).getName());
        assertTrue(userDetails2.getAuthorities().contains(role));
        assertEquals(1, userDetails2.getAuthorities().size());
    }

    /**
     * Checks if null password is correctly handled.
     */
    @Test
    void validateNullPassword() {
        String password = null;
        assertFalse(userDetails1.validate(password));
    }

    /**
     * Checks if correct password is correctly handled.
     */
    @Test
    void validateCorrectPassword() {
        String password = u1.getPassword();
        assertTrue(userDetails1.validate(password));
    }

    /**
     * Checks if incorrect password is correctly handled.
     */
    @Test
    void validateIncorrectPassword() {
        String password = "incorrect_password";
        assertFalse(userDetails1.validate(password));
    }

    /**
     * Test for password getter.
     */
    @Test
    void getPassword() {
        assertEquals(u1.getPassword(), userDetails1.getPassword());
        assertEquals(u2.getPassword(), userDetails2.getPassword());
    }

    /**
     * Test for username/netId getter.
     */
    @Test
    void getUsername() {
        assertEquals(u1.getNetId(), userDetails1.getUsername());
        assertEquals(u2.getNetId(), userDetails2.getUsername());
    }

    /**
     * Test for expiration of account.
     */
    @Test
    void isAccountNonExpired() {
        assertTrue(userDetails1.isAccountNonExpired());
    }

    /**
     * Test for locked state of account.
     */
    @Test
    void isAccountNonLocked() {
        assertTrue(userDetails1.isAccountNonLocked());
    }

    /**
     * Test for expiration of account credentials.
     */
    @Test
    void isCredentialsNonExpired() {
        assertTrue(userDetails1.isCredentialsNonExpired());
    }

    /**
     * Test for enabled state of account.
     */
    @Test
    void isEnabled() {
        assertTrue(userDetails1.isEnabled());
    }
}