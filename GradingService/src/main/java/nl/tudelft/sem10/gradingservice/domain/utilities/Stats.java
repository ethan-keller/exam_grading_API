package nl.tudelft.sem10.gradingservice.domain.utilities;

import java.util.Collection;
import java.util.Iterator;

public class Stats {

    /**
     * Logic to get the mean of a collection.
     *
     * @param items a collection
     * @return mean of the items
     */
    public static float mean(Collection<Float> items) {
        float sum = 0.0f;
        for (float item : items) {
            sum += item;
        }

        return sum / items.size();
    }

    /**
     * fr.
     *
     * @param items a
     * @return a
     */
    public static float variance(Collection<Float> items) {
        float variance = 0.0f;

        for (float item : items) {
            variance += Math.pow(Stats.mean(items) - item, 2);
        }

        return variance / items.size();
    }

    /**
     * .
     *
     * @param items a
     * @param weights a
     * @return a
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
     * .
     *
     * @param items a
     * @return a
     */
    public static double sum(Collection<Double> items) {
        double sum = 0.0;
        for (Double item : items) {
            sum += item;
        }

        return sum;
    }
}
