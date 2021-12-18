package com.oop.evolution;

import java.util.Arrays;

public class World {

    public static void main(String[] args) {
        BorderedMap borderedMap = new BorderedMap(20, 20, 0.5, 15);
        int initialEnergyLevel = 10;
        int moveEnergy = 1;
        Vector2d position = new Vector2d(1, 1);
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
        int[][] animalFieldCount = borderedMap.getAnimalFieldCount();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(animalFieldCount[i][j] + " ");
            }
            System.out.println();
        }

        Genome genome = new Genome();
        int[] counter = new int[8];
        for (int i = 0; i < 10000; i++) {
            int a = genome.generateMove();
            counter[a] += 1;
            dog.move(a);
            borderedMap.generateGrass();
            dog.fatigue();
        }

        System.out.println(dog.getEnergyLevel());
        System.out.println(Arrays.toString(counter));
        System.out.println(dog.getPosition() + " " + dog.getDirection());
        animalFieldCount = borderedMap.getAnimalFieldCount();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(animalFieldCount[i][j] + " ");
            }
            System.out.println();
        }
    }
}


