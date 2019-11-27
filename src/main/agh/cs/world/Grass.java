package agh.cs.world;

import agh.cs.movement.Vector2d;

public class Grass {
    private Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return this.position;
    }
}
