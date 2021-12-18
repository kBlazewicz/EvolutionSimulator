package com.oop.evolution;

public class BorderedMap extends AbstractMap {

    protected BorderedMap(int width, int height, double jungleRatio, int plantEnergySource) {
        super(width, height, jungleRatio, plantEnergySource);

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(position.hasGreaterParameter(getSize())
                || position.hasSmallerParameter(new Vector2d(0, 0)));
    }
}
