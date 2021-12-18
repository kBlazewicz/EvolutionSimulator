package com.oop.evolution;

public class Plant {

    private final Vector2d position;

    private final IWorldMap map;

    private final int energySource;

    Plant(Vector2d position, IWorldMap map, int plantEnergy) {
        this.position = position;
        this.map = map;
        energySource = plantEnergy;
    }

    public int energyBoost() {
        return energySource;
    }
}
