package com.oop.evolution;

import java.util.ArrayList;
import java.util.Random;

public class Animal {

    private Vector2d position;

    private final IWorldMap map;

    private final int initialEnergyLevel;

    private int energyLevel;

    private MapDirection direction = MapDirection.generateRandomDirection();

    private final int fatigueEnergyLoss;

    private ArrayList<Animal> childList = new ArrayList<>(0);

    //TODO Genome
    //private Genome genome;

    private ArrayList<IPositionChangeObserver> positionChangeObserversList = new ArrayList<IPositionChangeObserver>(0);

    Animal(IWorldMap map, Vector2d position, int initialEnergyLevel, int moveEnergy) {
        this.map = map;
        this.position = position;
        this.initialEnergyLevel = initialEnergyLevel;
        this.energyLevel = initialEnergyLevel;
        fatigueEnergyLoss = moveEnergy;
    }

    public Vector2d getPosition() {
        return position;
    }


    public void move(int direction) {
        if (direction > 7 || direction < 0) {
            throw new IllegalArgumentException("You can't turn for the value that is not in scope <0,7>");
        }

        if (direction == 0) {
            makeMove(position.add(this.direction.toUnitVector()));
        } else if (direction < 5) {
            for (int i = 0; i < direction; i++) {
                this.direction = this.direction.next();
            }
        } else {
            for (int i = 7; i >= direction; i--) {
                this.direction = this.direction.previous();
            }
        }
    }

    public void eat(Plant plant) {
        energyLevel += plant.energyBoost();
    }

    public void fatigue() {
        energyLevel -= fatigueEnergyLoss;
    }

    private void makeMove(Vector2d newPosition) {
        if (map.canMoveTo(newPosition)) {
            positionChanged(this.position, newPosition);
            this.position = newPosition;
        }
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : positionChangeObserversList) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }


}
