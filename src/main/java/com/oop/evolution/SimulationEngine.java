package com.oop.evolution;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {

    private final AbstractMap map;

    private final int startEnergy;

    private final int moveEnergy;

    private int date = 0;

    private final ArrayList<IMapChangeObserver> mapChangeListeners = new ArrayList<>();

    private boolean shutdownRequested = false;

    private boolean pause = true;

    private final ExecutorService service = Executors.newCachedThreadPool();


    public SimulationEngine(int width, int height, int startEnergy, int moveEnergy, int animalsAmount, double jungleRatio, int plantEnergySource,boolean isBordered) {
       if(isBordered) {
           this.map = new BorderedMap(width, height, jungleRatio, plantEnergySource);
       }
       else{
           this.map = new GlobeMap(width,height,jungleRatio,plantEnergySource);
       }
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        for (int i = 0; i < animalsAmount; i++) {
            generateAnimal();
        }
        run();
    }

    public void run() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                while (map.getNumberOfAnimals() > 0 && !shutdownRequested) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (!pause && map.getNumberOfAnimals() > 0 && !shutdownRequested) {
                        try {
                            nextSimulationDay();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                System.out.println("All animals are dead");
            }
        });
    }


    public void shutdown() {
        shutdownRequested = true;
        service.shutdown();
        System.out.println("enginge was shutdowned");
    }

    public void stop() {
        this.pause = true;
    }

    public void start() {
        this.pause = false;
    }


    public int getDate() {
        return date;
    }

    public AbstractMap getMap() {
        return map;
    }


    private void nextSimulationDay() throws InterruptedException {
        System.out.println(date);
        map.clear(date);

        ArrayList<Animal> animalArrayList = map.getAnimalArrayList();
        if (!animalArrayList.isEmpty()) {
            for (Animal animal : animalArrayList) {
                animal.move();
            }

            checkForBreeding();
        }

        map.generateGrass();
        date++;
        notifyMapChange();
    }

    public void addMapChangeListener(IMapChangeObserver object) {
        mapChangeListeners.add(object);
    }

    public int averageEnergy(){
        ArrayList<Animal> animals = map.getAnimalArrayList();
        int energy=0;
        for(Animal animal : animals){
            energy+=animal.getEnergyLevel();
        }
        if(energy==0) return 0;

        return energy/animals.size();
    }

    public int averageAmountOfChildren(){
        ArrayList<Animal> animals = map.getAnimalArrayList();
        int average = 0;
        for(Animal animal : animals){
           average+=animal.getChildrenAmount();
        }
        if(average==0) return 0;

        return average/animals.size();
    }

    public int averageLifeTime(){
        return map.averageLifeTime();
    }

    private void notifyMapChange() throws InterruptedException {
        for (IMapChangeObserver listener : mapChangeListeners) {
            listener.refreshMap();
        }
    }

    private void generateAnimal() {
        Vector2d size = map.getSize();
        Random random = new Random();
        int coordinateX = random.nextInt(size.x+1);
        int coordinateY = random.nextInt(size.y+1);

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
                }
            }
        }

    }

}


