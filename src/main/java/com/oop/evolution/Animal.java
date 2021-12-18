package com.oop.evolution;

import java.util.ArrayList;
import java.util.Random;

public class Animal {

    private Vector2d position;

    private final AbstractMap map;

    private final int initialEnergyLevel;

    private int energyLevel;

    private MapDirection direction = MapDirection.generateRandomDirection();

    private final int fatigueEnergyLoss;

    private final ArrayList<IPositionChangeObserver> observerList = new ArrayList<IPositionChangeObserver>(0);

    private final Genome genome = new Genome();

    public Animal(AbstractMap map, Vector2d position, int initialEnergyLevel, int moveEnergy) {
        this.map = map;
        this.position = position;
        this.initialEnergyLevel = initialEnergyLevel;
        this.energyLevel = initialEnergyLevel;
        fatigueEnergyLoss = moveEnergy;
        this.map.place(this);
    }

    public Genome getGenome() {
        return genome;
    }

    public void move(int direction) {
        if (direction > 7 || direction < 0) {
            throw new IllegalArgumentException("You can't turn for the value that is not in scope <0,7>");
        }

        if (direction == 0) {
            makeMove(position.add(this.direction.toUnitVector()));
        } else if (direction == 7) {
            makeMove(position.subtract(this.direction.toUnitVector()));
        } else if (direction < 5) {
            for (int i = 0; i < direction; i++) {
                this.direction = this.direction.next();
            }
        } else {
            for (int i = 6; i >= direction; i--) {
                this.direction = this.direction.previous();
            }
        }
    }

    public MapDirection getDirection() {
        return direction;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void eat(Plant plant) {
        energyLevel += plant.energyBoost();
        map.plantDestroy(position);
    }

    public void consumption(Vector2d position) {
        if (map.isPlantOnField(position)) {
            this.eat(map.plantAt(position));
        }
    }

    public void fatigue() {
        energyLevel -= fatigueEnergyLoss;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public int getInitialEnergyLevel() {
        return initialEnergyLevel;
    }

    public int getFatigueEnergyLoss() {
        return fatigueEnergyLoss;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    void addObserver(IPositionChangeObserver observer) {
        if (observerList.contains(observer)) {
            return;
        }
        observerList.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        observerList.remove(observer);
    }

    private void makeMove(Vector2d newPosition) {
        if (map.canMoveTo(newPosition)) {
            positionChanged(newPosition);
            this.position = newPosition;
            consumption(newPosition);
        }
    }

    private void positionChanged(Vector2d newPosition) {
        for (IPositionChangeObserver observer : observerList) {
            observer.positionChanged(this, newPosition);
        }
    }

}
