package com.oop.evolution;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d newPosition);
}
