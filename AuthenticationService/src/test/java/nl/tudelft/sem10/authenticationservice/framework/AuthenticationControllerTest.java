package nl.tudelft.sem10.authenticationservice.framework;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import nl.tudelft.sem10.authenticationservice.application.User;
import nl.tudelft.sem10.authenticationservice.domain.JwtRequest;
import nl.tudelft.sem10.authenticationservice.domain.UserDetailsImpl;
import nl.tudelft.sem10.authenticationservice.domain.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    private final transient User user = new User("jSnow", "pass123#", 0);
    private final transient UserDetailsImpl userDetails =
            Mockito.mock(UserDetailsImpl.class);
    private final transient UserDetailsServiceImpl service =
            Mockito.mock(UserDetailsServiceImpl.class);
    @Autowired
    private transient MockMvc mvc;
    @Autowired
    private transient AuthenticationController controller;
    @Autowired
    private transient PasswordEncoder passwordEncoder;

    /**
     * Set up and inject mock of user details service.
     *
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field serviceField = controller.getClass().getDeclaredField("userDetailsService");
        serviceField.setAccessible(true);
        serviceField.set(controller, service);
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
     * NetId exists, but user credentials do not match.
     *
     * @throws Exception if http communication fails
     */
    @Test
    void invalidatedUsername() throws Exception {
        when(service.loadUserByUsername(user.getNetId())).thenReturn(userDetails);
        when(userDetails.validate(user.getPassword())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders
                .get(GET_TOKEN_ENDPOINT)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(JSON_TOKEN_PATH_EXPRESSION).doesNotExist());
    }

    /**
     * NetId exists and user credentials match.
     *
     * @throws Exception if http communication fails
     */
    @Test
    void validatedUsername() throws Exception {
        when(service.loadUserByUsername(user.getNetId())).thenReturn(userDetails);
        when(userDetails.validate(user.getPassword())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .get(GET_TOKEN_ENDPOINT)
                .content(asJsonString(
                        new JwtRequest(user.getNetId(), user.getPassword(), user.getRoleInteger())
                ))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_TOKEN_PATH_EXPRESSION).exists())
                .andExpect(jsonPath(JSON_TOKEN_PATH_EXPRESSION).isNotEmpty());
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