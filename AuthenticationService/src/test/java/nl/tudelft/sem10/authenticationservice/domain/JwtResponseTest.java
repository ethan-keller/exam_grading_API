package nl.tudelft.sem10.authenticationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for JwtResponse.
 */
class JwtResponseTest {

    // transient for PMD
    private transient JwtResponse jwtResponse;

    /**
     * Define a JwtResponse.
     */
    @BeforeEach
    void setUp() {
        jwtResponse = new JwtResponse("thisisatoken123");
    }

    /**
     * Getter for token test.
     */
    @Test
    void getToken() {
        assertEquals("thisisatoken123", jwtResponse.getToken());
    }
}