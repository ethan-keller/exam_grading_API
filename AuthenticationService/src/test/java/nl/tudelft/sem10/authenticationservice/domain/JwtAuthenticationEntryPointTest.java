package nl.tudelft.sem10.authenticationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AccountExpiredException;

/**
 * Test suite for the JwtAuthenticationEntryPoint class.
 */
class JwtAuthenticationEntryPointTest {

    /**
     * Fields that facilitate console output reading.
     */
    private final transient ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final transient ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final transient PrintStream originalOut = System.out;
    private final transient PrintStream originalErr = System.err;

    /**
     * Fields that imitate and/or mock method parameters needed.
     */
    private final transient JwtAuthenticationEntryPoint entry = new JwtAuthenticationEntryPoint();
    private final transient HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
    private final transient HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
    private final transient AccountExpiredException exception =
            new AccountExpiredException("testing purposes");

    /**
     * Before each test the output streams are customized.
     */
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    /**
     * After each test the output streams get reset.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }


    /**
     * Test for the commence method.
     */
    @Test
    void commence() {
        try {
            // call method
            entry.commence(req, resp, exception);
            // verify that error has been sent
            verify(resp, Mockito.times(1))
                    .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            // check console output
            assertEquals("HTTP Status 401 - " + exception.getMessage()
                    + System.getProperty("line.separator"), outContent.toString());
        } catch (Exception e) {
            // if exception => fail
            e.printStackTrace();
            fail();
        }
    }
}
