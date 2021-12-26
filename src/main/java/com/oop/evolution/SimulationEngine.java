package com.oop.evolution;

import java.util.ArrayList;
import java.util.Random;

public class SimulationEngine {

    private final AbstractMap map;

    private final int startEnergy;

    private final int moveEnergy;

    private final int date = 0;

//    private final ArrayList<Animal> animalNotMovingToday = new ArrayList<>();


    public SimulationEngine(int width, int height, int startEnergy, int moveEnergy, int animalsAmount, double jungleRatio, int plantEnergySource) {
        this.map = new BorderedMap(width, height, jungleRatio, plantEnergySource);
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        for (int i = 0; i < animalsAmount; i++) {
            generateAnimal();
        }
    }

    public void runEngine() {
        while (map.getNumberOfAnimals() > 0 && date < 2000) {
            this.nextSimulationDay();
        }
    }

    private void nextSimulationDay() {
        map.clear();

        ArrayList<Animal> animalArrayList = map.getAnimalArrayList();
        if (!animalArrayList.isEmpty()) {
            checkForBreeding();
            for (Animal animal : animalArrayList) {
                animal.move();
            }
        }

        map.generateGrass();

    }


    private void generateAnimal() {
        Vector2d size = map.getSize();
        Random random = new Random();
        int coordinateX = random.nextInt(size.x);
        int coordinateY = random.nextInt(size.y);

        Vector2d position = new Vector2d(coordinateX, coordinateY);
        Animal animal = new Animal(map, position, startEnergy, moveEnergy);
    }

    private void checkForBreeding() {
        int[][] animalsCount = map.getAnimalFieldCount();

        for (int r = 0; r < animalsCount.length; r++) {
            for (int c = 0; c < animalsCount[0].length; c++) {
                if (animalsCount[r][c] > 2) {
                    Breeding breeder = new Breeding(new Vector2d(r, c), map);
                    breeder.classicBreeding();
//                    if (breaded.length > 1) {
//                        animalNotMovingToday.add(breaded[0]);
//                        animalNotMovingToday.add(breaded[1]);
//                    }
                }
            }
        }

    }

}


