package nl.tudelft.sem10.gradingservice.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nl.tudelft.sem10.gradingservice.domain.utilities.JsonParser;
import nl.tudelft.sem10.gradingservice.domain.utilities.Stats;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    /**
     * Clip a double value between a lower bound and an upper bound.
     *
     * @param jsonString json containing the value to clip
     * @param field      name of the json field containing the value
     * @param lb         lower bound
     * @param ub         upper bound
     * @return clipped value
     * @throws JSONException if json parsing fails
     */
    public static float jsonStringClip(String jsonString, String field, float lb, float ub)
            throws JSONException {
        JSONObject jsonObject = JsonParser.stringToJson(jsonString);
        float item = (float) jsonObject.getDouble(field);
        return Stats.clip(item, lb, ub);
    }

    /**
     * Computes weighted average of grades with corresponding weights.
     *
     * @param jsonString json map containing category names and weights
     * @param grades     collection of grades
     * @return weighted average
     * @throws JSONException if json parsing fails
     */
    public static double jsonWeightedAverage(String jsonString, Collection<Grade> grades) throws JSONException {
        Collection<Double> weights = new ArrayList<>();

        JSONObject jsonObject = JsonParser.stringToJson(jsonString);
        for (Grade grade : grades) {
            if (jsonObject.has(grade.getGradeType())) {
                weights.add(jsonObject.getDouble(grade.getGradeType()));
            } else {
                weights.add(Double.NaN);
            }
        }

        List<Double> items = grades.stream().map(x -> (double) x.getMark())
                .collect(Collectors.toList());

        return Stats.weightedAverage(items, weights);
    }
}
