package com.oop.evolution;

import java.util.HashMap;
import java.util.Map;

//class taken from https://stackoverflow.com/questions/20327958/random-number-with-probabilities/20328491

public class RandomWithDistribution {

    private final Map<Integer, Double> distribution;

    private double distSum;

    public RandomWithDistribution() {
        distribution = new HashMap<>();
    }

    public void addNumber(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getDistributedRandomNumber() {
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }

}
