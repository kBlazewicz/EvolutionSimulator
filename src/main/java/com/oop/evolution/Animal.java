package com.oop.evolution;

import java.util.ArrayList;

import static java.lang.Math.floor;

public class Animal {

    private Vector2d position;

    private final AbstractMap map;

    private final int initialEnergyLevel;

    private int energyLevel;

    private final int fatigueEnergyLoss;

    private MapDirection direction = MapDirection.generateRandomDirection();

    private final ArrayList<IPositionChangeObserver> positionChangeObservers = new ArrayList<>(0);

    private final Genome genome = new Genome();

    private final ArrayList<Animal> childrenList = new ArrayList<>();

    private int childrenAmount=0;

    private int deathTime;


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

    public void move() {
        int direction = genome.generateMove();

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
        fatigue();

    }

    private void makeMove(Vector2d newPosition) {
            newPosition = map.moveTo(newPosition,this.position);
            positionChanged(newPosition);
            this.position = newPosition;
    }

    public int getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(int deathTime) {
        this.deathTime = deathTime;
    }

    public void eat(Plant plant, int animalsEating) {
        energyLevel += (int) floor(plant.energyBoost() / animalsEating);
        map.plantDestroy(position);
    }

    public void consumption(Vector2d position) {
        if (map.isPlantOnField(position)) {
            ArrayList<Animal> animals = map.animalsAt(position);
            if (animals.size() > 1) {
                int animalsEating = 1;
                for (Animal animal : animals) {
                    if (animal.getEnergyLevel() > energyLevel) {
                        return;
                    } else if (animal.getEnergyLevel() == energyLevel) {
                        animalsEating += 1;
                    }
                }
                this.eat(map.plantAt(position), animalsEating);
            } else {
                this.eat(map.plantAt(position), 1);

            }
        }
    }

    public void fatigue() {
        energyLevel -= fatigueEnergyLoss;
    }

    public MapDirection getDirection() {
        return direction;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public double getEnergyPercentage(){
        return (double)energyLevel/initialEnergyLevel;
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

    public void addPositionObserver(IPositionChangeObserver observer) {
        if (positionChangeObservers.contains(observer)) {
            return;
        }
        positionChangeObservers.add(observer);
    }

    public void addChild(Animal child){
        childrenList.add(child);
        childrenAmount+=1;
    }

    public int getChildrenAmount(){
        return childrenAmount;
    }

    private void positionChanged(Vector2d newPosition) {
        for (IPositionChangeObserver observer : positionChangeObservers) {
            observer.positionChanged(this, newPosition);
        }
    }

}
