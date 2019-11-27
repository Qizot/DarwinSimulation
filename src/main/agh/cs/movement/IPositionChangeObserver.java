package agh.cs.movement;


import agh.cs.animal.Animal;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d oldPosition);
}
