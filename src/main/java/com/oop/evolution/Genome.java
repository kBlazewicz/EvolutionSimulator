package com.oop.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.round;

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

    public void genomeFromParents(Animal animal1, Animal animal2) {
        Random number = new Random();
        int strongerSide = number.nextInt(2);
        if (strongerSide == 1) {
            Animal temprary = animal1;
            animal1 = animal2;
            animal2 = temprary;
        }

        int genomesFromFirst = round(32 * animal1.getEnergyLevel() /
                (animal2.getEnergyLevel() + animal1.getEnergyLevel()));
        ArrayList<Integer> genome1 = animal1.getGenome().getGenome();
        ArrayList<Integer> genome2 = animal2.getGenome().getGenome();

        for (int i = 0; i < genomesFromFirst; i++) {
            this.genome.set(i, genome1.get(i));
        }

        for (int i = genomesFromFirst; i < 32; i++) {
            this.genome.set(i, genome2.get(i));
        }
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



