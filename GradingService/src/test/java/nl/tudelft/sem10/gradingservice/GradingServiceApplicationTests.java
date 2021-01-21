package nl.tudelft.sem10.gradingservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGradeEndpoint() throws Exception {
        // Test if an endpoint exists
        mockMvc.perform(get("/grade")
            .contentType("application/json"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void main() {
        GradingServiceApplication.main(new String[] {});
    }

    /*
    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void insertTest() throws Exception {
        // Insert element into DB
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/teacher")
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

    @Disabled
    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deleteTest() throws Exception {
        // Insert an element, get it back and check if all values are correct
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/teacher")
            .contentType("application/json")
            .content(String.valueOf(objNew)))
            .andExpect(status().isOk());

        // Delete element from DB and see if it's actually gone
        mockMvc.perform(delete("/teacher?courseCode=CSE2425&netid=testId&gradeType=endterm")
            .accept("application/json"))
            .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/grade")
            .contentType("application/json"))
            .andExpect(status().is4xxClientError())
            .andReturn();

        String content = "[" + result.getResponse().getContentAsString() + "]";

        JSONArray jsonArray = new JSONArray(new JSONTokener(content));
        JSONException thrown = assertThrows(
            JSONException.class,
            () -> jsonArray.getJSONObject(0),
            "Expected getJSONObject(0) to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("JSON"));
    }

    @Disabled
    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateTestSmallerValue() throws Exception {
        // Insert first element into DB, check all values are correct
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/teacher")
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
        mockMvc.perform(put("/teacher?courseCode=CSE2425&netid=testId&gradeType=endterm")
            .contentType("application/json")
            .content(String.valueOf(objNew)))
            .andExpect(status().isOk());

        // Check get with an ID instead of getting all grades
        result = mockMvc.perform(get("/grade?courseCode=CSE2425&netid=testId&gradeType=endterm")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();


        content = result.getResponse().getContentAsString();
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(1, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));
    }

    @Disabled
    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateTestBiggerValue() throws Exception {
        // Insert first element into DB, check all values are correct
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        mockMvc.perform(post("/teacher")
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
        mockMvc.perform(put("/teacher?courseCode=CSE2425&netid=testId&gradeType=endterm")
            .contentType("application/json")
            .content(String.valueOf(objNew)))
            .andExpect(status().isOk());

        // Check get with an ID instead of getting all grades
        result = mockMvc.perform(get("/grade?courseCode=CSE2425&netid=testId&gradeType=endterm")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();


        content = result.getResponse().getContentAsString();
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(1, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(7.99, jsonArray.getJSONObject(0).getDouble("mark"));
    }

    @Disabled
    @Test
    @SuppressWarnings("PMD")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void fancyGetTest() throws Exception {
        // Insert 3 grade objects into the database
        JSONObject objNew = new JSONObject();

        objNew.put("mark", 5.98);
        objNew.put("netid", "testId");
        objNew.put("course_code", "CSE2425");
        objNew.put("grade_type", "endterm");

        JSONObject objNew2 = new JSONObject();

        objNew2.put("mark", 7.587);
        objNew2.put("netid", "testId2");
        objNew2.put("course_code", "CSE4425");
        objNew2.put("grade_type", "midterm");

        JSONObject objNew3 = new JSONObject();

        objNew3.put("mark", 1.3);
        objNew3.put("netid", "testId");
        objNew3.put("course_code", "CSE2425");
        objNew3.put("grade_type", "midterm");

        mockMvc.perform(post("/teacher")
            .contentType("application/json")
            .content(String.valueOf(objNew)))
            .andExpect(status().isOk());
        mockMvc.perform(post("/teacher")
            .contentType("application/json")
            .content(String.valueOf(objNew2)))
            .andExpect(status().isOk());
        mockMvc.perform(post("/teacher")
            .contentType("application/json")
            .content(String.valueOf(objNew3)))
            .andExpect(status().isOk());

        // Test get with just netid
        MvcResult result = mockMvc.perform(get("/grade?netid=testId")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(2, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));
        assertEquals("testId", jsonArray.getJSONObject(1).getString("netid"));
        assertEquals(1.3, jsonArray.getJSONObject(1).getDouble("mark"));

        // Test get with course code
        result = mockMvc.perform(get("/grade?courseCode=CSE2425")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

        content = result.getResponse().getContentAsString();
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(2, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));
        assertEquals("testId", jsonArray.getJSONObject(1).getString("netid"));
        assertEquals(1.3, jsonArray.getJSONObject(1).getDouble("mark"));

        // Test get with course code and grade type
        result = mockMvc.perform(get("/grade?courseCode=CSE2425&gradeType=endterm")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

        content = result.getResponse().getContentAsString();
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(1, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));

        // Test get with course code, grade type and netid
        result = mockMvc.perform(get("/grade?courseCode=CSE2425&gradeType=endterm&netid=testId")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

        content = result.getResponse().getContentAsString();
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(1, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));


        // Test get with course code, grade type and netid
        result = mockMvc.perform(get("/grade?courseCode=CSE2425&netid=testId")
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

        content = result.getResponse().getContentAsString();
        jsonArray = new JSONArray(new JSONTokener(content));

        assertEquals(2, jsonArray.length());
        assertEquals("testId", jsonArray.getJSONObject(0).getString("netid"));
        assertEquals(5.98, jsonArray.getJSONObject(0).getDouble("mark"));
        assertEquals("endterm", jsonArray.getJSONObject(0).getString("gradeType"));

        assertEquals("testId", jsonArray.getJSONObject(1).getString("netid"));
        assertEquals(1.3, jsonArray.getJSONObject(1).getDouble("mark"));
        assertEquals("midterm", jsonArray.getJSONObject(1).getString("gradeType"));

    }
     */
}

