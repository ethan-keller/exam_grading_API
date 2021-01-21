package nl.tudelft.sem10.authenticationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class UtilityTest {

    @Test
    void hash() throws NoSuchAlgorithmException {
        String toBeHashed = "pass";
        String expected =
            "5b722b307fce6c944905d132691d5e4a22"
                + "14b7fe92b738920eb3fce3a90420a19511c3010a0e7712b054d"
                + "aef5b57bad59ecbd93b3280f210578f547f4aed4d25";

        assertEquals(expected, Utility.hash(toBeHashed));
    }
}