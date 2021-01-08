package nl.tudelft.sem10.gradingservice.domain.utilities;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import nl.tudelft.sem10.gradingservice.domain.Utility;

public class Stats {

    /**
     * Logic to get the mean of a collection.
     *
     * @param items a collection
     * @return mean of the items
     */
    public static float mean(Collection<Float> items) {
        float sum = (float) Utility.sum(items.stream().map(x -> (double) x)
                .collect(Collectors.toList()));
        return sum / items.size();
    }

    /**
     * Calculates the variance of a given collection of floats.
     *
     * @param items - Collection of floats
     * @return - the variance of type Float
     */
    public static float variance(Collection<Float> items) {
        float variance = 0.0f;

        for (float item : items) {
            variance += Math.pow(Stats.mean(items) - item, 2);
        }

        return variance / items.size();
    }

    /**
     * Calculates the weighted average of a collection of items.
     *
     * @param items - Collection of Doubles
     * @param weights - Collection of Doubles
     * @return - the weighted average of type double
     */
    public static double weightedAverage(Collection<Double> items, Collection<Double> weights) {
        if (items.size() != weights.size()) {
            return -1;
        }

        double result = 0.0;
        Iterator<Double> iterItems = items.iterator();
        Iterator<Double> iterWeights = weights.iterator();

        while (iterItems.hasNext() && iterWeights.hasNext()) {
            result += iterItems.next() * iterWeights.next();
        }

        return result / sum(weights);
    }

    /**
     * Calculates the sum of the given collection of items.
     *
     * @param items - Collection of Doubles
     * @return - the sum of type double
     */
    public static double sum(Collection<Double> items) {
        double sum = 0.0;
        for (Double item : items) {
            sum += item;
        }

        return sum;
    }
}
