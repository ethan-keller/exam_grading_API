package nl.tudelft.sem10.authenticationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for JwtRequest.
 */
class JwtRequestTest {

    // transient for PMD
    private transient JwtRequest jwtRequest;

    /**
     * Define a JwtRequest.
     */
    @BeforeEach
    void setUp() {
        jwtRequest = new JwtRequest("netId", "password123", 0);
    }

    /**
     * Default constructor for json parsing.
     */
    @Test
    void defaultConstructor() {
        JwtRequest req = new JwtRequest();
        assertNotNull(req);
    }

    /**
     * Getter for netId test.
     */
    @Test
    void getNetId() {
        assertEquals("netId", jwtRequest.getNetId());
    }

    /**
     * Getter for password test.
     */
    @Test
    void getPassword() {
        assertEquals("password123", jwtRequest.getPassword());
    }

    /**
     * Getter for type test.
     */
    @Test
    void getType() {
        assertEquals(0, jwtRequest.getType());
    }
}