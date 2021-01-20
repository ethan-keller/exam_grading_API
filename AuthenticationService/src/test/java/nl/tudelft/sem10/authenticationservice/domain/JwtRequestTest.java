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
    private transient JwtRequest jwtRequestStudent;
    private transient JwtRequest jwtRequestTeacher;

    /**
     * Define a JwtRequest.
     */
    @BeforeEach
    void setUp() {
        jwtRequestStudent = new JwtRequest("netId_s", "password123", 0);
        jwtRequestTeacher = new JwtRequest("netId_t", "password123", 1);
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
        assertEquals("netId_s", jwtRequestStudent.getNetId());
        assertEquals("netId_t", jwtRequestTeacher.getNetId());
    }

    /**
     * Getter for password test.
     */
    @Test
    void getPassword() {
        assertEquals("password123", jwtRequestStudent.getPassword());
        assertEquals("password123", jwtRequestTeacher.getPassword());
    }

    /**
     * Getter for type test.
     */
    @Test
    void getType() {
        assertEquals(0, jwtRequestStudent.getType());
        assertEquals(1, jwtRequestTeacher.getType());
    }
}