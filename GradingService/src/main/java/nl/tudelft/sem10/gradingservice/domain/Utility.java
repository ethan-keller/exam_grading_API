package nl.tudelft.sem10.gradingservice.domain;

import java.util.Collection;
import nl.tudelft.sem10.gradingservice.domain.utilities.JsonParser;
import nl.tudelft.sem10.gradingservice.domain.utilities.Stats;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    /* ***** MATH UTILITIES ****** */

    public static double mean(Collection<Float> items) {
        return Stats.mean(items);
    }

    public static double variance(Collection<Float> items) {
        return Stats.variance(items);
    }

    public static double weightedAverage(Collection<Double> items, Collection<Double> weights) {
        return Stats.weightedAverage(items, weights);
    }

    public static double sum(Collection<Double> items) {
        return Stats.sum(items);
    }


    /* ***** JSON UTILITIES ****** */

    public static String jsonToString(Object o) {
        return JsonParser.asJsonString(o);
    }

    public static JSONObject stringToJson(String jsonString) throws JSONException {
        return JsonParser.stringToJson(jsonString);
    }


    /* ***** OTHER FUTURE UTILITIES ****** */


}
