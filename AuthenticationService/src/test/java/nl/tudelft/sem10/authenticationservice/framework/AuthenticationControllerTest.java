package nl.tudelft.sem10.authenticationservice.framework;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Test suite for the authentication controller
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthenticationController controller;

    private transient String getTokenEndpoint;
    private final transient User user = new User("jSnow", "pass123#", 0);
    private final transient UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
    private final transient UserDetailsServiceImpl service = Mockito.mock(UserDetailsServiceImpl.class);

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        getTokenEndpoint = "/getToken";

        Field serviceField = controller.getClass().getDeclaredField("userDetailsService");
        serviceField.setAccessible(true);
        serviceField.set(controller, service);
    }

    @Test
    void contextLoads() {
        assertNotNull(mvc);
    }

    @Test
    void nonExistingUsername() throws Exception {
        when(service.loadUserByUsername(user.getNetId())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders
                .get(getTokenEndpoint)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void invalidatedUsername() throws Exception {
        when(service.loadUserByUsername(user.getNetId())).thenReturn(userDetails);
        when(userDetails.validate(user.getNetId())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders
                .get(getTokenEndpoint)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void validatedUsername() throws Exception {
        when(service.loadUserByUsername(user.getNetId())).thenReturn(userDetails);
        when(userDetails.validate(user.getPassword())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .get(getTokenEndpoint)
                .content(asJsonString(
                        new JwtRequest(user.getNetId(), user.getPassword(), user.getRoleInteger())
                ))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }



    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}