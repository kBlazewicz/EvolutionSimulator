package com.oop.evolution;

public interface IWorldMap extends IPositionChangeObserver {

    boolean canMoveTo(Vector2d position);

    boolean place(Animal animal);


}