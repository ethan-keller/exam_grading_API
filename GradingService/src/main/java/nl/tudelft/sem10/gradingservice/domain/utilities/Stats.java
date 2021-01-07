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
    public static double mean(Collection<Double> items) {
        double sum = 0.0;
        for (Double item : items) {
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
    public static double variance(Collection<Double> items) {
        double mean = Stats.mean(items);
        double variance = 0.0;

        for (Double item : items) {
            variance += (mean - item) * (mean - item);
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

        while (iterItems.hasNext()) {
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
