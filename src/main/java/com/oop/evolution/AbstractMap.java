package com.oop.evolution;

import java.util.*;

public abstract class AbstractMap implements IWorldMap, IEnergyObserver {

    private final int[][] animalFieldCount;

    private final Vector2d size;

    final LinkedHashMap<Animal, Vector2d> animalsMap = new LinkedHashMap<>();

    private final LinkedHashMap<Vector2d, Plant> plantsMap = new LinkedHashMap<>();

    private final Jungle jungle;

    private final int plantEnergySource; //needed for planting

    private final ArrayList<Vector2d> jungleFreePlantSpaces = new ArrayList<>(0);

    private final ArrayList<Vector2d> normalFreePlantSpaces = new ArrayList<>(0);

    private int numberOfAnimals = 0;

    protected AbstractMap(int width, int height, double jungleRatio, int plantEnergySource) {
        size = new Vector2d(width, height);
        this.jungle = new Jungle(this, jungleRatio);
        this.plantEnergySource = plantEnergySource;
        makePlantSpaceArrays();
        animalFieldCount = new int[width + 1][height + 1];
    }

    public Vector2d getSize() {
        return size;
    }

    public void placePlant(Vector2d position, Plant plant) {
        if (plantsMap.containsKey(position)) {
            return;
        }
        plantsMap.put(position, plant);
    }

    public void generateGrass() {
        if (!normalFreePlantSpaces.isEmpty()) {
            Random number = new Random();
            int index = number.nextInt(normalFreePlantSpaces.size());
            Vector2d normalPlantVector = normalFreePlantSpaces.get(index);
            normalFreePlantSpaces.remove(index);
            placePlant(normalPlantVector, new Plant(normalPlantVector, this, plantEnergySource));
        }

        if (!jungleFreePlantSpaces.isEmpty()) {
            Random number = new Random();
            int index = number.nextInt(jungleFreePlantSpaces.size());
            Vector2d junglePlantVector = jungleFreePlantSpaces.get(index);
            jungleFreePlantSpaces.remove(index);
            placePlant(junglePlantVector, new Plant(junglePlantVector, this, plantEnergySource));
        }
    }

    public void plantDestroy(Vector2d position) {
        if (plantsMap.containsKey(position)) {
            if (jungle.isJungle(position)) {
                jungleFreePlantSpaces.add(position);
            } else {
                normalFreePlantSpaces.add(position);
            }
            plantsMap.remove(position);
        }
    }

    public boolean isPlantOnField(Vector2d position) {
        return plantsMap.containsKey(position);
    }


    private void makePlantSpaceArrays() {
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                Vector2d position = new Vector2d(x, y);
                if (jungle.isJungle(position)) {
                    jungleFreePlantSpaces.add(position);
                } else {
                    normalFreePlantSpaces.add(position);
                }
            }
        }
    }

    public int[][] getAnimalFieldCount() {
        return animalFieldCount;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void newBorn(){
        numberOfAnimals+=1;
    }

    private void rotting(Animal animal) {
        System.out.println("population of animals is decreasing");
        Vector2d position = animal.getPosition();
        if(animalFieldCount[position.x][position.y]>0) {
            numberOfAnimals-=1;
            animalFieldCount[position.x][position.y] -= 1;
        }

        animalsMap.remove(animal);
        animal.removePositionObserver(this);
        animal.removeEnergyObserver(this);
    }

    @Override
    public Plant plantAt(Vector2d vector2d) {
        return plantsMap.get(vector2d);
    }

    @Override
    public ArrayList<Animal> animalsAt(Vector2d vector2d) {
        ArrayList<Animal> animals = new ArrayList<>(0);
        Collection<Vector2d> vectors = animalsMap.values();
        Collection<Animal> animalsIn = animalsMap.keySet();

        for(Animal animal : animalsIn){
            if(animalsMap.get(animal).equals(vector2d))animals.add(animal);
        }
        return animals;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }

    @Override
    public boolean place(Animal animal) {
        numberOfAnimals+=1;
        Vector2d position = animal.getPosition();
        animalsMap.put(animal,position);
        animalFieldCount[position.x][position.y] += 1;
        animal.addPositionObserver(this);
        animal.addEnergyObserver(this);
        return true;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d newPosition) {
        Vector2d oldPosition = animal.getPosition();
        animalsMap.remove(animal);
        animalsMap.put(animal, newPosition);
        animalFieldCount[oldPosition.x][oldPosition.y] -= 1;
        animalFieldCount[newPosition.x][newPosition.y] += 1;
    }

    @Override
    public void energyUpdate(Animal animal) {
        if (animal.getEnergyLevel() < 1) {
            rotting(animal);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractMap)) return false;
        AbstractMap map = (AbstractMap) o;
        return Objects.equals(animalsMap, map.animalsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalsMap);
    }

}


