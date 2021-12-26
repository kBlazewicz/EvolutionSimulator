package com.oop.evolution;

import java.util.ArrayList;

import static java.lang.Math.round;

public class Breeding {

    private final Vector2d position;

    private final ArrayList<Animal> animalList;

    private final AbstractMap map;

    public Breeding(Vector2d position, AbstractMap map) {
        this.position = position;
        this.animalList = map.animalsAt(position);
        this.map = map;
    }

    public void classicBreeding() {
        if (animalList.size() < 2) {
            return;
        }
        Animal animal1 = animalList.get(0);
        Animal animal2 = animalList.get(1);


        for (Animal animal : animalList) {
            if (animal.getEnergyLevel() > animal1.getEnergyLevel() && !animal.equals(animal2)) {
                animal2 = animal1;
                animal1 = animal;
            }
        }

        if (animal2.getEnergyLevel() >= 0.5 * animal2.getInitialEnergyLevel()) {
            Animal child = new Animal(map, position, animal1.getInitialEnergyLevel(), animal1.getFatigueEnergyLoss());
            child.getGenome().genomeFromParents(animal1, animal2);
            animal1.setEnergyLevel((int) round(animal1.getEnergyLevel() * 0.75));
            animal2.setEnergyLevel((int) round(animal2.getEnergyLevel() * 0.75));
            int energy = (int) round(animal1.getEnergyLevel() * 0.25) + (int) round(animal2.getEnergyLevel() * 0.25);
            child.setEnergyLevel(energy);
        }
    }
}
