package nl.tudelft.sem10.gradingservice.domain;

import java.util.Collection;
import nl.tudelft.sem10.gradingservice.domain.utilities.JsonParser;
import nl.tudelft.sem10.gradingservice.domain.utilities.Stats;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    /* ***** MATH UTILITIES ****** */

    /**
     * Calculates the mean of a given collection of floats.
     *
     * @param items - Collection of floats
     * @return - the mean of type Float
     */
    public static Float mean(Collection<Float> items) {
        return Stats.mean(items);
    }

    /**
     * Calculates the variance of a given collection of floats.
     *
     * @param items - Collection of floats
     * @return - the variance of type Float
     */
    public static Float variance(Collection<Float> items) {
        return Stats.variance(items);
    }

    /**
     * Calculates the weighted average of a collection of items.
     *
     * @param items - Collection of Doubles
     * @param weights - Collection of Doubles
     * @return - the weighted average of type double
     */
    public static double weightedAverage(Collection<Double> items, Collection<Double> weights) {
        return Stats.weightedAverage(items, weights);
    }

    /**
     * Calculates the sum of the given collection of items.
     *
     * @param items - Collection of Doubles
     * @return - the sum of type double
     */
    public static double sum(Collection<Double> items) {
        return Stats.sum(items);
    }


    /* ***** JSON UTILITIES ****** */

    /**
     * Method that converts an Object, JSONObject in most cases, to a String.
     *
     * @param o - Object to convert
     * @return String
     */
    public static String jsonToString(Object o) {
        return JsonParser.asJsonString(o);
    }

    /**
     * Method to convert a String into a JSONObject.
     *
     * @param jsonString - The String to convert
     * @return JSONObject
     * @throws JSONException - if the operation fails at some point
     */
    public static JSONObject stringToJson(String jsonString) throws JSONException {
        return JsonParser.stringToJson(jsonString);
    }


    /* ***** OTHER FUTURE UTILITIES ****** */


}
