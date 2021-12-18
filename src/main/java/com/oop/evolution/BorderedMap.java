package com.oop.evolution;

public class BorderedMap extends AbstractMap {


    public BorderedMap(int width, int height) {
        super(width, height);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(position.hasGreaterParameter(getSize())
                || position.hasSmallerParameter(new Vector2d(0, 0)));
    }
}
