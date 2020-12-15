package nl.tudelft.sem10.userservice.domain;

import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class UtilityTest {

  @Autowired
  @MockBean
  private transient RestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    this.restTemplate = mock(RestTemplate.class);
  }

  @Test
  void hash() throws NoSuchAlgorithmException {
    String toBeHashed = "pass";
    String expected = "5b722b307fce6c944905d132691d5e4a2214b7fe92b738920eb3fce3a90420a19511c3010a0e7712b054daef5b57bad59ecbd93b3280f210578f547f4aed4d25";

    assertEquals(expected, Utility.hash(toBeHashed));
  }

  @Test
  void getEncryptedPassword() throws NoSuchAlgorithmException {
    String password = "pass";
    when(restTemplate.getForObject("http://localhost:8080/encode/{password}", String.class, Utility.hash(password))).thenReturn("encryptedPassword");
    String response = Utility.getEncryptedPassword(password, restTemplate);

    assertEquals("encryptedPassword", response);

  }
}