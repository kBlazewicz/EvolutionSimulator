package com.oop.evolution;

public class BorderedMap extends AbstractMap {

    protected BorderedMap(int width, int height, double jungleRatio, int plantEnergySource) {
        super(width, height, jungleRatio, plantEnergySource);

    }

    @Override
    public Vector2d moveTo(Vector2d position, Vector2d oldPosition) {
        if ((position.hasGreaterParameter(getSize())
                || position.hasSmallerParameter(new Vector2d(0, 0)))) {

            return oldPosition;
        }

        return position;
    }
}
