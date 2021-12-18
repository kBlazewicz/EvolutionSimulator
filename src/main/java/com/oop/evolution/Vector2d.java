package com.oop.evolution;

import java.util.Objects;

public class Vector2d {
    public final int y;

    public final int x;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x > other.x && this.y > other.y;
    }

    public boolean followsOrEquals(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean hasSmallerParameter(Vector2d other) {
        return this.x < other.x || this.y < other.y;
    }

    public boolean hasGreaterParameter(Vector2d other) {
        return this.x > other.x || this.y > other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }
        if (this == other) {
            return true;
        }
        return (((Vector2d) other).x == this.x && ((Vector2d) other).y == this.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public int y() {
        return y;
    }

    public int x() {
        return x;
    }
}
