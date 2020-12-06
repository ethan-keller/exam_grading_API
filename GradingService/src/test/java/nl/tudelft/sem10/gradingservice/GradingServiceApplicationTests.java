package nl.tudelft.sem10.gradingservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


/**
 * BuildingController test.
 * test passes if path leads to a 200 OK response
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestPropertySource(locations = "classpath:./application-dev.properties")
@SuppressWarnings("unused")
public class GradingServiceApplicationTests {

    @Autowired
    private transient MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Grade grade;

    //@Test
    void testBuildingEndpoint() throws Exception {
        mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    //@Test
    void crudGradeTest() throws Exception {
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");


        JSONObject objNew2 = new JSONObject();

        objNew2.put("mark", 9.45);
        objNew2.put("netid", "testId2");
        objNew2.put("course_code", "CSE4567");
        objNew2.put("grade_type", "midterm");

        /*
        Submit 2 grade entities and check if that works
         */
        mockMvc.perform(post("/grade")
                .contentType("application/json")
                .content(String.valueOf(objNew)))
                .andExpect(status().isOk());
        MvcResult result = mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(post("/grade")
                .contentType("application/json")
                .content(String.valueOf(objNew2)))
                .andExpect(status().isOk());
        MvcResult result2 = mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(delete("/grade/2")
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());

        /*
        Get the currently stored entities in the database and check the values
         */
        String content = result.getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(new JSONTokener(content));
        assertEquals(1, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));
        assertEquals("CSE2425", jsonArray.getJSONObject(0).getString("courseCode"));
        assertEquals("endterm", jsonArray.getJSONObject(0).getString("gradeType"));
        JSONException thrown = assertThrows(
                JSONException.class,
                () -> jsonArray.getJSONObject(1),
                "Expected getJSONObject(1) to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("JSON"));

        JSONObject objNew3 = new JSONObject();

        /*
        Test upgrade of a mark; first invalid then valid
         */
        objNew3.put("mark", 0.587);
        mockMvc.perform(put("/grade/1")
                .contentType("application/json")
                .content(String.valueOf(objNew3)))
                .andExpect(status().isOk());

        MvcResult result3 = mockMvc.perform(get("/grade/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String content2 = result.getResponse().getContentAsString();

        JSONArray jsonArray2 = new JSONArray(new JSONTokener(content2));
        assert(jsonArray2.getJSONObject(0).getDouble("mark") > objNew3.getDouble("mark"));

        objNew3.put("mark", 9.99);
        mockMvc.perform(put("/grade/1")
                .contentType("application/json")
                .content(String.valueOf(objNew3)))
                .andExpect(status().isOk());

        MvcResult result4 = mockMvc.perform(get("/grade/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String content3 = "[" + result4.getResponse().getContentAsString() + "]";

        JSONArray jsonArray3 = new JSONArray(new JSONTokener(content3));
        assert(jsonArray3.getJSONObject(0).getDouble("mark") == objNew3.getDouble("mark"));
    }
}
