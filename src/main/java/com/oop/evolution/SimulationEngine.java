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


    public SimulationEngine(int width, int height, int startEnergy, int moveEnergy, int animalsAmount, double jungleRatio, int plantEnergySource) {
        this.map = new BorderedMap(width, height, jungleRatio, plantEnergySource);
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        for (int i = 0; i < animalsAmount; i++) {
            generateAnimal();
        }
        run();
    }

    public void run(){
        service.submit(new Runnable() {
            @Override
            public void run() {
                while (map.getNumberOfAnimals() > 0 && !shutdownRequested) {
                    try {
                        Thread.sleep(1000);
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
                            Thread.sleep(400);
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

    //TODO make private
    public void nextSimulationDay() throws InterruptedException {
        System.out.println(date);
        map.clear();

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


