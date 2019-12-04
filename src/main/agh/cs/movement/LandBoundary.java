package agh.cs.movement;

import agh.cs.movement.Vector2d;

public class LandBoundary {
    private Vector2d lowerLeft;
    private  Vector2d upperRight;

    public LandBoundary(Vector2d lowerLeft, Vector2d upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    @Override
    public String toString() {
        return String.format("Lower left: %s Upper right: %s", lowerLeft, upperRight);
    }

    public boolean contains(Vector2d pos) {
        return lowerLeft.precedes(pos) && upperRight.follows(pos);
    }

    public Vector2d getUpperRight() {
        return this.upperRight;
    }

    public Vector2d getLowerLeft() {
        return this.lowerLeft;
    }
}
