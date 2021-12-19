package com.oop.evolution;

import java.util.Arrays;

public class World {

    public static void main(String[] args) {

        int initialEnergyLevel = 10;
        int moveEnergy = 1;
        SimulationEngine engine = new SimulationEngine(20,20,20,10,20,0.5);



        for(int i=0;i<4;i++) {
            engine.nextSimulationDay();
        }

//        AbstractMap map = new BorderedMap(20,20,0.5,5);
//        Animal animal = new Animal(map,new Vector2d(0,0),10,10);
//        System.out.println(map.animalsAt(new Vector2d(0,0)));
//        animal.setEnergyLevel(-1);
//        System.out.println(map.animalsAt(new Vector2d(0,0)));

    }
}


