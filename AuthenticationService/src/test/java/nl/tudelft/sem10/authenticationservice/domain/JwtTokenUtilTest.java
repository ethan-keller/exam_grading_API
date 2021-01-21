package nl.tudelft.sem10.authenticationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.Clock;
import java.util.Date;
import nl.tudelft.sem10.authenticationservice.application.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Test suite for the token utility class.
 */
class JwtTokenUtilTest {

    private final transient UserDetails details1 =
        new UserDetailsImpl(new User("jSnow", "pass123!", 0));
    private final transient UserDetails details2 =
        new UserDetailsImpl(new User("aBeller", "pass456@", 1));
    private final transient long validity = 24 * 60 * 60;
    private transient JwtTokenUtil utils;
    private transient String token1;
    private transient String token2;
    private transient Clock clock;

    /**
     * Makes sure secret is injected in the class before utilizing it.
     *
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // mock the clock to return a specific (frozen) time when called on
        clock = Mockito.mock(Clock.class);
        final long currentMillis = System.currentTimeMillis();
        when(clock.millis()).thenReturn(currentMillis);

        utils = new JwtTokenUtil();

        // set dummy secret
        String dummySecret = "abcdefghijklm";

        // set dummy secret in secret field
        Field secretField = utils.getClass().getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(utils, dummySecret);

        // set mocked clock in clock field
        setClockField();

        // generate tokens
        token1 = utils.generateToken(details1);
        token2 = utils.generateToken(details2);
    }

    /**
     * Sets the clock field in the utility class to the mocked clock.
     *
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    private void setClockField() throws IllegalAccessException, NoSuchFieldException {
        Field clockField = utils.getClass().getDeclaredField("clock");
        clockField.setAccessible(true);
        clockField.set(utils, clock);
    }

    /**
     * Test for netId from token getter.
     */
    @Test
    void getNetIdFromToken() {
        assertEquals("jSnow", utils.getNetIdFromToken(token1));
        assertEquals("aBeller", utils.getNetIdFromToken(token2));
    }

    /**
     * Test for expiration date from token getter.
     */
    @Test
    void getExpirationDateFromToken() {
        long d1 = (new Date(clock.millis() + 1000 * validity)).getTime() / 1000;
        long d2 = utils.getExpirationDateFromToken(token1).getTime() / 1000;
        assertEquals(d1, d2);
    }


    /**
     * Test for the token validation method.
     */
    @Test
    void validateToken() throws NoSuchFieldException, IllegalAccessException {
        assertTrue(utils.validateToken(token1, details1));
        assertFalse(utils.validateToken(token2, details1));
        assertFalse(utils.validateToken(token1, details2));

        // mock clock to ensure expiration for testing purposes
        long expiredTime = System.currentTimeMillis() + 1000 * validity;
        when(clock.millis()).thenReturn(expiredTime);

        // set new clock
        setClockField();

        assertFalse(utils.validateToken(token1, details1));
    }

    /**
     * Exception handling when getting netId from token.
     */
    @Test
    void exceptionHandlingGetNetIdFromToken() {
        assertNull(utils.getNetIdFromToken("invalidToken"));
    }

    /**
     * Exception handling when validating token.
     */
    @Test
    void exceptionHandlingTokenValidation() {
        assertFalse(utils.validateToken("invalidToken",
            new UserDetailsImpl(new User("test", "test", 0))));
    }
}