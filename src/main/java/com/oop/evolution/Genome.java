package com.oop.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Genome {

    private final ArrayList<Integer> genome = generateGenomes();

    public ArrayList<Integer> getGenome() {
        return genome;
    }

    public int generateMove() {
        double[] distribution = movePreferences();
        RandomWithDistribution generator = new RandomWithDistribution();

        for (int i = 0; i < 8; i++) {
            generator.addNumber(i, distribution[i]);
        }

        return generator.getDistributedRandomNumber();
    }

    private double[] movePreferences() {

        double[] distribution = new double[8];

        for (Integer genome : genome) {
            distribution[genome] += 1;
        }

        for (int i = 0; i < 8; i++) {
            distribution[i] /= 32;
        }

        return distribution;
    }

    private ArrayList<Integer> generateGenomes() {
        ArrayList<Integer> genome = new ArrayList<>(32);
        for (int i = 0; i < 32; i++) {
            Random number = new Random();
            int gene = number.nextInt(8);
            genome.add(gene);
        }
        Collections.sort(genome);
        return genome;
    }

}



