package agh.cs.world;

import agh.cs.movement.Vector2d;

public class Grass {
    private Vector2d position;
    private int energy;

    public Grass(Vector2d position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String toString() {
        return "X";
    }
}
