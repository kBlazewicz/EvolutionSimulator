package com.oop.evolution;

import java.util.ArrayList;

public interface IWorldMap extends IPositionChangeObserver {

    boolean canMoveTo(Vector2d position);

    boolean place(Animal animal);

    ArrayList<Animal> objectsAt(Vector2d vector2d);

    Vector2d getSize();

}