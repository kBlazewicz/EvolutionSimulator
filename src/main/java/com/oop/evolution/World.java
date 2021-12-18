package com.oop.evolution;

import java.util.Arrays;

public class World {

    public static void main(String[] args) {
        BorderedMap borderedMap = new BorderedMap(100, 100);
        int initialEnergyLevel = 10;
        int moveEnergy = 10;
        Vector2d position = new Vector2d(5, 5);
        Animal dog = new Animal(borderedMap, position, initialEnergyLevel, moveEnergy);

//        System.out.println(dog.getDirection());
//        dog.move(4);
//        System.out.println(dog.getDirection());
//        dog.move(3);
//        System.out.println(dog.getDirection());
//
//        System.out.println(dog.getPosition());
//        dog.move(0);
//        System.out.println(dog.getPosition());
//        dog.move(7);
//

        //count number of moves in each direction if there is 10000 moves
        //to check randomness of them

        Genome genome = new Genome();
        int[] counter = new int[8];
        for (int i = 0; i < 10000; i++) {
            int  a = genome.generateMove();
            counter[a]+=1;
            dog.move(a);
        }
        System.out.println(Arrays.toString(counter));
        System.out.println(dog.getPosition()+" " +dog.getDirection());
    }
}


