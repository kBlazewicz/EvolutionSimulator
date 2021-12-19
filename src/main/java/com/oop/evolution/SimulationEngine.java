package com.oop.evolution;

import java.util.ArrayList;
import java.util.Random;

public class SimulationEngine implements IEnergyObserver{

    private final AbstractMap map;

    private final int startEnergy;

    private final int moveEnergy;

    private final ArrayList<Animal> animalArrayList = new ArrayList<>();

    private int date = 0;

    private final ArrayList<Animal> animalNotMovingToday = new ArrayList<>();


    public SimulationEngine(int width, int height, int startEnergy, int moveEnergy, int animalsAmount, double jungleRatio) {
        this.map = new BorderedMap(width, height, jungleRatio, 200);
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        for (int i = 0; i < animalsAmount; i++) {
            animalArrayList.add(generateAnimal());
        }
    }

    public void nextSimulationDay() {
        if(!animalArrayList.isEmpty()) {
//        checkForBreeding();
            for (Animal animal : animalArrayList) {
                if (!animalNotMovingToday.contains(animal)) {
                    animal.move();
                }
            }
//        map.generateGrass();
//        animalNotMovingToday.clear();   //TODO good for breeding statistics
        }
        //TODO to be removed, for backend test only

        if (date % 50 == 0) {
            int[][] animalFieldCount = map.getAnimalFieldCount();
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    System.out.print(animalFieldCount[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println(map.getNumberOfAnimals());
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }
        date += 1;

    }


    private Animal generateAnimal() {
        Vector2d size = map.getSize();
        Random random = new Random();
        int coordinateX = random.nextInt(size.x);
        int coordinateY = random.nextInt(size.y);

        Vector2d position = new Vector2d(coordinateX, coordinateY);
        Animal animal = new Animal(map, position, startEnergy, moveEnergy);
        animal.addEnergyObserver(this);
        return animal;
    }

    private void checkForBreeding() {
        Vector2d size = map.getSize();
        int[][] animalsCount = map.getAnimalFieldCount();

        for (int r = 0; r < size.x; r++) {
            for (int c = 0; c < size.y; c++) {
                if (animalsCount[r][c] > 2) {
                    Breeding breeder = new Breeding(new Vector2d(r, c), map);
                    Animal[] breaded = breeder.classicBreeding();
                    if (breaded.length > 1) {
                        animalNotMovingToday.add(breaded[0]);
                        animalNotMovingToday.add(breaded[1]);
                    }
                }
            }
        }

    }

    @Override
    public void energyUpdate(Animal animal) {
        if (animal.getEnergyLevel() < 1) {
            System.out.println(animalArrayList.size());
            animalArrayList.remove(animal);
            animal.removeEnergyObserver(this);
        }
    }
}


