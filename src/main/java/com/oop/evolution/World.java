package com.oop.evolution;

public class World {

    public static void main(String[] args) {
        SimulationEngine engine = new SimulationEngine(20, 20, 20, 1, 150, 0.5, 5);
        engine.runEngine();
    }
}



