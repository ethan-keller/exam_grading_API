package nl.tudelft.sem10.gradingservice.domain.utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    /**
     * Transform object to JSON string.
     *
     * @param obj JSON object to transform
     * @return JSON string
     */
    public static String asJsonString(JSONObject obj) {
        try {
            return obj.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to convert a String into a JSONObject.
     *
     * @param jsonString - The String to convert
     * @return JSONObject
     * @throws JSONException - if the operation fails at some point
     */
    public static JSONObject stringToJson(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }
}
