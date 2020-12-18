package nl.tudelft.sem10.authenticationservice.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import nl.tudelft.sem10.authenticationservice.application.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Test suite for the rest service class.
 */
class RestServiceTest {

    private static final int USER_SERVICE_PORT = 8083;
    private static final String GET_USER_ENDPOINT = "/user";
    private final transient RestService rest = new RestService();
    private final transient RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private final transient User user = new User("jSnow", "pass123#", 0);

    /**
     * Set up and mocked rest template field injection.
     *
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifier block access
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field restTemplateField = rest.getClass().getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(rest, restTemplate);
    }

    /**
     * NetId parameter is null.
     */
    @Test
    void nullNetId() {
        assertNull(rest.getUserFromUserService(null));
    }

    /**
     * NetId is valid, but communication fails.
     */
    @Test
    void validNetIdBadCommunication() {
        String url = "http://localhost:" + USER_SERVICE_PORT + GET_USER_ENDPOINT
                + "/" + user.getNetId();

        ResponseEntity<User> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.getForEntity(url, User.class)).thenReturn(response);

        assertNull(rest.getUserFromUserService(user.getNetId()));
    }

    /**
     * NetId is valid and communication is successful.
     */
    @Test
    void validNetIdGoodCommunication() {
        String url = "http://localhost:" + USER_SERVICE_PORT + GET_USER_ENDPOINT
                + "/" + user.getNetId();

        ResponseEntity<User> response = new ResponseEntity<>(user, HttpStatus.OK);
        when(restTemplate.getForEntity(url, User.class)).thenReturn(response);

        assertEquals(user, rest.getUserFromUserService(user.getNetId()));
    }

    /**
     * Exception handling.
     */
    @Test
    void exceptionHandling() {
        String url = "http://localhost:" + USER_SERVICE_PORT + GET_USER_ENDPOINT
                + "/" + user.getNetId();
        when(restTemplate.getForEntity(url, User.class)).thenThrow(RestClientException.class);
        assertNull(rest.getUserFromUserService(user.getNetId()));
    }
}