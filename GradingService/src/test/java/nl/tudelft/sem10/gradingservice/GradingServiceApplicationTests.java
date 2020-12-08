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
import org.springframework.test.annotation.DirtiesContext;
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

    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void testBuildingEndpoint() throws Exception {
        // Test if an endpoint exists
        mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void insertTest() throws Exception {
        // Insert element into DB
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/grade")
                .contentType("application/json")
                .content(String.valueOf(objNew)))
                .andExpect(status().isOk());
        MvcResult result = mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(new JSONTokener(content));

        // Check if all values gotten back are in fact correct
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
    }

    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void deleteTest() throws Exception {
        // Insert an element, get it back and check if all values are correct
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/grade")
                .contentType("application/json")
                .content(String.valueOf(objNew)))
                .andExpect(status().isOk());

        // Delete element from DB and see if it's actually gone
        mockMvc.perform(delete("/grade/1")
                .accept("application/json"))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(new JSONTokener(content));
        JSONException thrown = assertThrows(
                JSONException.class,
                () -> jsonArray.getJSONObject(0),
                "Expected getJSONObject(0) to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("JSON"));
    }

    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void updateTestSmallerValue() throws Exception {
        // Insert first element into DB, check all values are correct
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/grade")
                .contentType("application/json")
                .content(String.valueOf(objNew)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(new JSONTokener(content));
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));

        // Update the grade in the DB with a smaller mark, check if the value stayed the same
        objNew.put("mark", 4.99);
        mockMvc.perform(put("/grade/1")
                .contentType("application/json")
                .content(String.valueOf(objNew)))
                .andExpect(status().isOk());

        // Check get with an ID instead of getting all grades
        result = mockMvc.perform(get("/grade/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();


        content = "[" + result.getResponse().getContentAsString() + "]";
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(1, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));
    }

    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void updateTestBiggerValue() throws Exception {
        // Insert first element into DB, check all values are correct
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/grade")
                .contentType("application/json")
                .content(String.valueOf(objNew)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/grade")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        JSONArray jsonArray = new JSONArray(new JSONTokener(content));
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));

        // Update the grade in the DB with a higher mark, check if the value updated accordingly
        objNew.put("mark", 7.99);
        mockMvc.perform(put("/grade/1")
                .contentType("application/json")
                .content(String.valueOf(objNew)))
                .andExpect(status().isOk());

        // Check get with an ID instead of getting all grades
        result = mockMvc.perform(get("/grade/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();


        content = "[" + result.getResponse().getContentAsString() + "]";
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(1, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(7.99, jsonArray.getJSONObject(0).getDouble("mark"));
    }
}

