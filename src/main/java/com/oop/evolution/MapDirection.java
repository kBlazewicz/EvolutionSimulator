package com.oop.evolution;

import java.util.Random;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST;


    public static MapDirection parseFromNumber(int number) {
        return switch (number) {
            case 0 -> MapDirection.NORTH_EAST;
            case 1 -> MapDirection.EAST;
            case 2 -> MapDirection.SOUTH_EAST;
            case 3 -> MapDirection.SOUTH;
            case 4 -> MapDirection.SOUTH_WEST;
            case 5 -> MapDirection.WEST;
            case 6 -> MapDirection.NORTH_WEST;
            case 7 -> MapDirection.NORTH;
            default -> throw new IllegalArgumentException("Cannot parse MapDirection element" +
                    " from number that is not in scope <0,7>.");
        };
    }

    public static MapDirection generateRandomDirection() {
        Random random = new Random();
        int direction = random.nextInt(8);
        return parseFromNumber(direction);
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> MapDirection.NORTH_EAST;
            case NORTH_EAST -> MapDirection.EAST;
            case EAST -> MapDirection.SOUTH_EAST;
            case SOUTH_EAST -> MapDirection.SOUTH;
            case SOUTH -> MapDirection.SOUTH_WEST;
            case SOUTH_WEST -> MapDirection.WEST;
            case WEST -> MapDirection.NORTH_WEST;
            case NORTH_WEST -> MapDirection.NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> MapDirection.NORTH_WEST;
            case NORTH_EAST -> MapDirection.NORTH;
            case EAST -> MapDirection.NORTH_EAST;
            case SOUTH_EAST -> MapDirection.EAST;
            case SOUTH -> MapDirection.SOUTH_EAST;
            case SOUTH_WEST -> MapDirection.SOUTH;
            case WEST -> MapDirection.SOUTH_WEST;
            case NORTH_WEST -> MapDirection.WEST;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case EAST -> new Vector2d(1, 0);
            case WEST -> new Vector2d(-1, 0);
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case NORTH_WEST -> new Vector2d(-1, 1);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
        };

    }
}
