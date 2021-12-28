package com.oop.evolution;

import java.util.ArrayList;

public interface IWorldMap extends IPositionChangeObserver {

    Vector2d moveTo(Vector2d position,Vector2d oldPosition);

    boolean place(Animal animal);

    ArrayList<Animal> animalsAt(Vector2d vector2d);

    Plant plantAt(Vector2d vector2d);

    Vector2d getSize();

}