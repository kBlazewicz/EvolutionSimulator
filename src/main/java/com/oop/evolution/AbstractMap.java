package com.oop.evolution;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public abstract class AbstractMap implements IWorldMap{

    private final Vector2d size;


    private final LinkedHashMap<Animal,Vector2d> animalsMap = new LinkedHashMap<>();

    private final LinkedHashMap<Plant, Vector2d> plantsMap = new LinkedHashMap<>();

    protected AbstractMap(int width,int height) {
        size = new Vector2d(width,height);
    }

    public Vector2d getSize() {
        return size;
    }

    public boolean placePlant(Vector2d position, Plant plant){
        if (plantsMap.containsValue(position)){
            return false;
        }
        plantsMap.put(plant,position);
        return true;
    }

    @Override
    public ArrayList<Animal> objectsAt(Vector2d vector2d) {
        ArrayList<Animal> animals = new ArrayList<>(0);
        animalsMap.keySet().forEach(key->{ if(animalsMap.get(key)==vector2d)animals.add(key);});
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
        animalsMap.put(animal,newPosition);
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


