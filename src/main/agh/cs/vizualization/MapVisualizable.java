package agh.cs.vizualization;

import agh.cs.movement.Vector2d;

public interface MapVisualizable {
    Object objectAt(Vector2d pos);
    boolean isOccupied(Vector2d pos);
}
