package com.oop.evolution;

public class World {

    public static void main(String[] args){
        BorderedMap borderedMap = new BorderedMap(10,10);
        int initialEnergyLevel = 10;
        int moveEnergy = 10;
        Vector2d position = new Vector2d(5,5);
        Animal dog = new Animal(borderedMap,position,initialEnergyLevel,moveEnergy);

        System.out.println(dog.getDirection());
        dog.move(4);
        System.out.println(dog.getDirection());
        dog.move(3);
        System.out.println(dog.getDirection());

        System.out.println(dog.getPosition());
        dog.move(0);
        System.out.println(dog.getPosition());
        dog.move(7);



    }
}


