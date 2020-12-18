package nl.tudelft.sem10.authenticationservice.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import nl.tudelft.sem10.authenticationservice.application.User;
import nl.tudelft.sem10.authenticationservice.domain.JwtTokenUtil;
import nl.tudelft.sem10.authenticationservice.domain.UserDetailsImpl;
import nl.tudelft.sem10.authenticationservice.domain.UserDetailsServiceImpl;
import nl.tudelft.sem10.authenticationservice.domain.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Test suite for the authentication controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    private static final String GET_TOKEN_ENDPOINT = "/getToken";
    private static final String JSON_TOKEN_PATH_EXPRESSION = "$.token";
    private static final User teacher = new User("aBrook", "pass345&", 1);
    private final transient User user = new User("jSnow", "pass123#", 0);
    private final transient String bearerToken = "Bearer thisisatoken123";
    private final transient String token = bearerToken.substring(7);
    private final transient UserDetailsImpl userDetails =
            Mockito.mock(UserDetailsImpl.class);
    private final transient UserDetailsServiceImpl service =
            Mockito.mock(UserDetailsServiceImpl.class);
    private final transient JwtTokenUtil tokenUtil =
            Mockito.mock(JwtTokenUtil.class);
    private final transient RestService rest =
            Mockito.mock(RestService.class);
    @Autowired
    private transient MockMvc mvc;
    @Autowired
    private transient AuthenticationController controller;
    @Autowired
    private transient PasswordEncoder passwordEncoder;

    /**
     * Set up and inject mocks.
     *
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field serviceField = controller.getClass().getDeclaredField("userDetailsService");
        serviceField.setAccessible(true);
        serviceField.set(controller, service);
        Field jwtTokenField = controller.getClass().getDeclaredField("jwtTokenUtil");
        jwtTokenField.setAccessible(true);
        jwtTokenField.set(controller, tokenUtil);
        Field restField = controller.getClass().getDeclaredField("rest");
        restField.setAccessible(true);
        restField.set(controller, rest);
    }

    /**
     * Test to check if the context loads correctly.
     */
    @Test
    void contextLoads() {
        assertNotNull(mvc);
    }

    /**
     * NetId does not exist.
     *
     * @throws Exception if http communication fails
     */
    @Test
    void nonExistingUsername() throws Exception {
        when(service.loadUserByUsername(user.getNetId())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders
                .get(GET_TOKEN_ENDPOINT)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(JSON_TOKEN_PATH_EXPRESSION).doesNotExist());
    }

    /**
     * Incorrect password when requesting token.
     *
     * @throws Exception if http communication fails
     */
    @Test
    void incorrectPassword() throws Exception {
        when(userDetails.getPassword()).thenReturn("incorrectPass");
        when(service.loadUserByUsername(user.getNetId())).thenReturn(userDetails);

        mvc.perform(MockMvcRequestBuilders
                .get(GET_TOKEN_ENDPOINT)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(JSON_TOKEN_PATH_EXPRESSION).doesNotExist());
    }

    /**
     * Correct credentials when requesting token.
     *
     * @throws Exception if http communication fails
     */
    @Test
    void correctCredentials() throws Exception {
        when(userDetails.getPassword()).thenReturn(
                passwordEncoder.encode(Utility.hash(user.getPassword())));
        when(service.loadUserByUsername(user.getNetId())).thenReturn(userDetails);
        when(tokenUtil.generateToken(userDetails)).thenReturn("token");

        mvc.perform(MockMvcRequestBuilders
                .get(GET_TOKEN_ENDPOINT)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_TOKEN_PATH_EXPRESSION).exists());
    }

    /**
     * Tests if encoding matches the password.
     */
    @Test
    void encodePassword() {
        String result = controller.encodePassword(user.getPassword()).getBody();
        passwordEncoder.matches(user.getPassword(), result);
    }

    /**
     * Validate token for nul netId.
     */
    @Test
    void nullNetId() {
        when(tokenUtil.getNetIdFromToken(anyString())).thenReturn(null);
        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND),
                controller.validateToken(anyString()));
    }

    /**
     * Validate token for null user.
     */
    @Test
    void nullUser() {
        when(tokenUtil.getNetIdFromToken(anyString())).thenReturn(user.getNetId());
        when(rest.getUserFromUserService(user.getNetId())).thenReturn(null);
        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND),
                controller.validateToken(anyString()));
    }

    /**
     * Validate token for valid student.
     */
    @Test
    void validStudent() {
        when(tokenUtil.getNetIdFromToken(anyString())).thenReturn(user.getNetId());
        when(rest.getUserFromUserService(user.getNetId())).thenReturn(user);
        assertEquals(ResponseEntity.ok(user.getRole().getName()),
                controller.validateToken(anyString()));
    }

    /**
     * Validate token for valid teacher.
     */
    @Test
    void validTeacher() {
        when(tokenUtil.getNetIdFromToken(anyString())).thenReturn(teacher.getNetId());
        when(rest.getUserFromUserService(teacher.getNetId())).thenReturn(teacher);
        assertEquals(ResponseEntity.ok(teacher.getRole().getName()),
                controller.validateToken(anyString()));
    }

    /**
     * No user found when validating token with netId.
     */
    @Test
    void noUserFound() {
        when(tokenUtil.getNetIdFromToken(token)).thenReturn(null);
        assertEquals(ResponseEntity.ok(false),
                controller.validateNetIdToken(user.getNetId(), bearerToken));
    }

    /**
     * NetId in token and netId from user do not match.
     */
    @Test
    void tokenFromOtherUser() {
        when(tokenUtil.getNetIdFromToken(token)).thenReturn(teacher.getNetId());
        assertEquals(ResponseEntity.ok(false),
                controller.validateNetIdToken(user.getNetId(), bearerToken));
    }

    /**
     * NetId in token and netId from user match.
     */
    @Test
    void matchingNetIds() {
        when(tokenUtil.getNetIdFromToken(token)).thenReturn(user.getNetId());
        assertEquals(ResponseEntity.ok(true),
                controller.validateNetIdToken(user.getNetId(), bearerToken));
    }

    /**
     * Exception thrown when getting NetID.
     */
    @Test
    void exceptionWhenValidating() {
        assertEquals(ResponseEntity.ok(false),
                controller.validateNetIdToken(user.getNetId(), bearerToken));
    }

    /**
     * Test helper method to transform object to JSON string.
     *
     * @param obj object to transform
     * @return JSON string
     */
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}