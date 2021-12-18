package com.oop.evolution;

import static java.lang.Math.round;

public class Jungle {

    private final Vector2d[] jungleCoordinates;

    private final IWorldMap map;


    public Jungle(IWorldMap map,double jungleRatio) {
        this.map = map;
        this.jungleCoordinates = generateJungle(jungleRatio);

    }

    public Vector2d[] getJungleCoordinates() {
        return jungleCoordinates;
    }

    public boolean isJungle(Vector2d position){
        return position.followsOrEquals(jungleCoordinates[0]) && position.precedes(jungleCoordinates[1]);
    }

    private Vector2d[] generateJungle(double jungleRatio) {
        Vector2d size = this.map.getSize();

        int width = (int) round(size.x * jungleRatio);
        int height = (int) round(size.y * jungleRatio);

        int leftX = round((size.x - width) / 2);
        int rightX = leftX + width;

        int downY = round((size.y - height) / 2);
        int upY = downY + height;

        return new Vector2d[]{new Vector2d(leftX, downY), new Vector2d(rightX, upY)};
    }

}
