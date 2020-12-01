package nl.tudelft.sem10.gradingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem10.gradingservice.entities.Grade;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/** BuildingController test.
 *  @ test passes if path leads to a 200 OK response
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestPropertySource(locations = "classpath:./application-dev.properties")
@SuppressWarnings("unused")
public class GradingServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Grade grade;

    @Test
    void testBuildingEndpoint() throws Exception {
        mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}

