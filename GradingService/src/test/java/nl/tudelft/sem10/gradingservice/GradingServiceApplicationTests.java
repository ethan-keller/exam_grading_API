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

    /*
    create new building, fetch it and check values
     */
//    @Test
//    void createBuildingTest() throws Exception {
//        JSONObject objNew = new JSONObject();
//
//        objNew.put("name", "testBuilding");
//        objNew.put("address", "testAddress");
//        objNew.put("numberOfBikes", 50);
//
//        JSONObject objNew2 = new JSONObject();
//
//        objNew2.put("name", "testBuilding2");
//        objNew2.put("address", "testAddress2");
//        objNew2.put("numberOfBikes", 30);
//
//        mockMvc.perform(post("/crud/insertbuilding")
//                .contentType("application/json")
//                .content(String.valueOf(objNew)))
//                .andExpect(status().isOk());
//        MvcResult result = mockMvc.perform(get("/building")
//                .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        mockMvc.perform(post("/crud/insertbuilding")
//                .contentType("application/json")
//                .content(String.valueOf(objNew2)))
//                .andExpect(status().isOk());
//        MvcResult result2 = mockMvc.perform(get("/building")
//                .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        mockMvc.perform(delete("/crud/deleteBuilding/2")
//                .contentType("application/json")
//                .accept("application/json"))
//                .andExpect(status().isOk());
//
//        String content = result.getResponse().getContentAsString();
//
//        JSONArray jsonArray = new JSONArray(new JSONTokener(content));
//        assertEquals(1, jsonArray.length());
//        assertEquals("testBuilding", jsonArray.getJSONObject(0).getString("buildingName"));
//        assertEquals("testAddress", jsonArray.getJSONObject(0).getString("buildingAddress"));
//        assertEquals(50, jsonArray.getJSONObject(0).getInt("buildingBikes"));
//        JSONException thrown = assertThrows(
//                JSONException.class,
//                () -> jsonArray.getJSONObject(1),
//                "Expected getJSONObject(1) to throw, but it didn't"
//        );
//
//        assertTrue(thrown.getMessage().contains("JSON"));
//    }
}

