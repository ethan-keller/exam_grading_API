package nl.tudelft.sem10.authenticationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.io.PrintStream;
import java.lang.reflect.Field;
import nl.tudelft.sem10.authenticationservice.application.User;
import nl.tudelft.sem10.authenticationservice.application.UserDetailsServiceImpl;
import nl.tudelft.sem10.authenticationservice.framework.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Test suite for the user details service.
 */
class UserDetailsServiceImplTest {

    private final transient UserDetailsService service = new UserDetailsServiceImpl();
    private final transient User user = new User("jSnow", "pass123#", 0);
    private final transient RestService rest = mock(RestService.class);

    /**
     * Sets up the tests by injecting mocked rest template.
     *
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field restField = service.getClass().getDeclaredField("rest");
        restField.setAccessible(true);
        restField.set(service, rest);
    }

    /**
     * netId exists.
     */
    @Test
    void loadExistingUsername() {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        when(rest.getUserFromUserService(user.getNetId())).thenReturn(user);
        assertEquals(userDetails, service.loadUserByUsername(user.getNetId()));
    }

    /**
     * Null netId.
     */
    @Test
    void loadNullUsername() {
        when(rest.getUserFromUserService(null)).thenReturn(null);
        assertNull(service.loadUserByUsername(user.getNetId()));
    }

    /**
     * NetId does not exist.
     */
    @Test
    void loadNonExistingUsername() {
        final PrintStream out = System.out;
        PrintStream mock = mock(PrintStream.class);
        System.setOut(mock);

        when(rest.getUserFromUserService(user.getNetId())).thenReturn(null);
        assertNull(service.loadUserByUsername(user.getNetId()));
        verify(mock, times(1)).println(anyString());

        System.setOut(out);
    }
}
