package com.oop.evolution;

import java.util.*;

public abstract class AbstractMap implements IWorldMap {

    //TODO delete animalFieldCount after making UI
    private final int[][] animalFieldCount;

    private final Vector2d size;

    protected final LinkedHashMap<Animal, Vector2d> animalsMap = new LinkedHashMap<>();

    private final LinkedHashMap<Vector2d, Plant> plantsMap = new LinkedHashMap<>();

    private final Jungle jungle;

    private final int plantEnergySource; //needed for planting

    private final ArrayList<Vector2d> jungleFreePlantSpaces = new ArrayList<>(0);

    private final ArrayList<Vector2d> normalFreePlantSpaces = new ArrayList<>(0);

    private final ArrayList<Animal> animalArrayList = new ArrayList<>();
    private final ArrayList<Animal> deadAnimals = new ArrayList<>();


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

    public void clear(int date) {
        ArrayList<Animal> toDelete = new ArrayList<>();
        for (Animal animal : animalsMap.keySet()) {
            if (animal.getEnergyLevel() <= 0) {
                toDelete.add(animal);
                animal.setDeathTime(date);
                deadAnimals.add(animal);
            }
        }

        for (Animal animal : toDelete) {
            animalsMap.remove(animal);
            animalArrayList.remove(animal);
            Vector2d position = animal.getPosition();
            if (animalFieldCount[position.x][position.y] > 0) {
                animalFieldCount[position.x][position.y] -= 1;
            }
        }

        for (Animal animal : animalsMap.keySet()) {
            animal.consumption(animal.getPosition());
        }
    }

    //ANIMALS
    public int averageLifeTime(){
        int time = 0;
        for(Animal animal: deadAnimals){
            time+=animal.getDeathTime();
        }
        if(time==0)return 0;
        return time/ deadAnimals.size();
    }

    public int getNumberOfAnimals() {
        return animalArrayList.size();
    }

    public ArrayList<Animal> getAnimalArrayList() {
        return animalArrayList;
    }

    public int[][] getAnimalFieldCount() {
        return animalFieldCount;
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        animalsMap.put(animal, position);
        animalFieldCount[position.x][position.y] += 1;
        animal.addPositionObserver(this);
        animalArrayList.add(animal);
        return true;
    }

    @Override
    public ArrayList<Animal> animalsAt(Vector2d vector2d) {
        ArrayList<Animal> animals = new ArrayList<>(0);
        Collection<Animal> animalsIn = animalsMap.keySet();

        for (Animal animal : animalsIn) {
            if (animalsMap.get(animal).equals(vector2d)) animals.add(animal);
        }
        return animals;
    }

    public Animal strongestAnimal(ArrayList<Animal> animals){
        Animal strongest = animals.get(0);
        for(Animal animal : animals){
            if (animal.getEnergyLevel() > strongest.getEnergyLevel()){
                strongest=animal;
            }
        }
        return strongest;
    }

    @Override
    public abstract Vector2d moveTo(Vector2d position,Vector2d oldPosition);

    @Override
    public void positionChanged(Animal animal, Vector2d newPosition) {
        Vector2d oldPosition = animal.getPosition();
        animalsMap.remove(animal);
        animalsMap.put(animal, newPosition);
        animalFieldCount[oldPosition.x][oldPosition.y] -= 1;
        animalFieldCount[newPosition.x][newPosition.y] += 1;
    }

    //PLANTING
    public boolean isJungle(Vector2d position){
        return jungle.isJungle(position);
    }

    public boolean isPlantOnField(Vector2d position) {
        return plantsMap.containsKey(position);
    }
    public int numberOfPlants(){
        return plantsMap.size();
    }

    public void generateGrass() {
        freePlantSpace(normalFreePlantSpaces);
        freePlantSpace(jungleFreePlantSpaces);
    }

    @Override
    public Plant plantAt(Vector2d vector2d) {
        return plantsMap.get(vector2d);
    }

    private void makePlantSpaceArrays() {
        for (int x = 0; x < size.x + 1; x++) {
            for (int y = 0; y < size.y + 1; y++) {
                Vector2d position = new Vector2d(x, y);
                if (jungle.isJungle(position)) {
                    jungleFreePlantSpaces.add(position);
                } else {
                    normalFreePlantSpaces.add(position);
                }
            }
        }
    }

    private void placePlant(Vector2d position, Plant plant) {

        if (plantsMap.containsKey(position)) {
            return;
        }
        plantsMap.put(position, plant);
    }

    private void freePlantSpace(ArrayList<Vector2d> normalFreePlantSpaces) {
        if (!normalFreePlantSpaces.isEmpty()) {
            Random number = new Random();
            int index = number.nextInt(normalFreePlantSpaces.size());
            Vector2d normalPlantVector = normalFreePlantSpaces.get(index);
            if (animalsAt(normalPlantVector).size() == 0) {
                normalFreePlantSpaces.remove(index);
                placePlant(normalPlantVector, new Plant(normalPlantVector, this, plantEnergySource));
            }
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


