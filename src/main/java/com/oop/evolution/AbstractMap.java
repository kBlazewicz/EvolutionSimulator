package com.oop.evolution;

import java.util.*;

public abstract class AbstractMap implements IWorldMap {

    private final Vector2d size;

    //the animal are first because they are unique key nad plants to keep the flow
    private final LinkedHashMap<Animal, Vector2d> animalsMap = new LinkedHashMap<>();

    private final LinkedHashMap<Plant, Vector2d> plantsMap = new LinkedHashMap<>();

    private final Jungle jungle;

    private final int plantEnergySource; //needed for planting

    private final ArrayList<Vector2d> jungleFreePlantSpaces = new ArrayList<>(0);

    private final ArrayList<Vector2d> normalFreePlantSpaces = new ArrayList<>(0);


    protected AbstractMap(int width, int height, double jungleRatio, int plantEnergySource) {
        size = new Vector2d(width, height);
        this.jungle = new Jungle(this, jungleRatio);
        this.plantEnergySource = plantEnergySource;
        makePlantSpaceArrays();
    }

    public Vector2d getSize() {
        return size;
    }

    public void placePlant(Vector2d position, Plant plant) {
        if (plantsMap.containsValue(position)) {
            return;
        }
        plantsMap.put(plant, position);
    }

    public void generateGrass() {
        if (!normalFreePlantSpaces.isEmpty()) {
            Random number = new Random();
            int index = number.nextInt(normalFreePlantSpaces.size());
            Vector2d normalPlantVector = normalFreePlantSpaces.get(index);
            normalFreePlantSpaces.remove(index);
            placePlant(normalPlantVector,new Plant(normalPlantVector,this,plantEnergySource));
        }

        if (!jungleFreePlantSpaces.isEmpty()) {
            Random number = new Random();
            int index = number.nextInt(jungleFreePlantSpaces.size());
            Vector2d junglePlantVector = jungleFreePlantSpaces.get(index);
            jungleFreePlantSpaces.remove(index);
            placePlant(junglePlantVector,new Plant(junglePlantVector,this,plantEnergySource));
        }
    }

    public void plantDestroy(Vector2d position,Plant plant){
        if(plantsMap.containsValue(position)) {
            if (jungle.isJungle(position)) {
                jungleFreePlantSpaces.add(position);
            } else {
                normalFreePlantSpaces.add(position);
            }
            plantsMap.remove(plant);
        }
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

    @Override
    public ArrayList<Animal> objectsAt(Vector2d vector2d) {
        ArrayList<Animal> animals = new ArrayList<>(0);
        animalsMap.keySet().forEach(key -> {
            if (animalsMap.get(key) == vector2d) animals.add(key);
        });
        return animals;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }

    @Override
    public boolean place(Animal animal) {
        animal.addObserver(this);
        return true;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d newPosition) {
        animalsMap.remove(animal);
        animalsMap.put(animal, newPosition);
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


