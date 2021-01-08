package nl.tudelft.sem10.gradingservice.domain.utilities;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Stats {

    /**
     * Logic to get the mean of a collection.
     *
     * @param items a collection
     * @return mean of the items
     */
    public static float mean(Collection<Float> items) {
        float sum = (float) sum(items.stream()
                .filter(x -> !x.isNaN())
                .map(x -> (double) x)
                .collect(Collectors.toList()));
        return sum / items.stream().filter(x -> !x.isNaN()).count();
    }

    /**
     * Calculates the variance of a given collection of floats.
     *
     * @param items - Collection of floats
     * @return - the variance of type Float
     */
    public static float variance(Collection<Float> items) {
        float variance = 0.0f;
        float mean = mean(items);

        for (Float item : items) {
            if (!item.isNaN()) {
                variance += Math.pow(mean - item, 2);
            }
        }

        return variance / items.stream().filter(x -> !x.isNaN()).count();
    }

    /**
     * Calculates the weighted average of a collection of items.
     *
     * @param items   - Collection of Doubles
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
            Double item = iterItems.next();
            Double weight = iterWeights.next();

            if (!weight.isNaN()) {
                result += item * weight;
            }
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
            if (!item.isNaN()) {
                sum += item;
            }
        }

        return sum;
    }

    /**
     * Clip a double value between a lower bound and an upper bound.
     *
     * @param item       value to clip
     * @param lowerBound lower bound
     * @param upperBound upper bound
     * @return clipped value
     */
    public static float clip(float item, float lowerBound, float upperBound) {
        if (item < lowerBound) {
            return lowerBound;
        } else {
            return Math.min(item, upperBound);
        }
    }
}
