package agh.cs.world;

import agh.cs.animal.Animal;
import agh.cs.movement.IPositionChangeObserver;
import agh.cs.movement.Vector2d;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorldMap implements IPositionChangeObserver {

    private int width;
    private int height;

    private Map<Vector2d, List<Animal>> animalMap = new LinkedHashMap<>();
    private Map<Vector2d, Grass> grassMap = new LinkedHashMap<>();

    public boolean canMoveTo(Vector2d pos) {
        List<Animal> animals = animalMap.get(pos);
        if (animals == null) return true;
        return animals.size() < 2;
    }

    public void place(Animal animal) {
        List<Animal> animals = animalMap.get(animal.getPosition());
        if (animals != null) {
            animals.add(animal);
            return;
        }
        List<Animal> newAnimals = new ArrayList<>();
        newAnimals.add(animal);
        animalMap.put(animal.getPosition(), newAnimals);
    }

    public boolean isOccupied(Vector2d pos) {
        List<Animal> animals = animalMap.get(pos);
        return !(animals == null && grassMap.get(pos) == null);
    }

    public Object objectAt(Vector2d pos) {
        List<Animal> animals = animalMap.get(pos);
        if (animals.isEmpty()) {
            return grassMap.get(pos);
        }
        return animals.get(0);
    }

    public void positionChanged(Animal animal, Vector2d oldPosition) {
        List<Animal> animals = animalMap.get(oldPosition);
        if (animals == null) {
            throw new IllegalArgumentException("no element has been found on position: " + oldPosition);
        }

        if (!animals.remove(animal)) {
            throw new IllegalArgumentException("animal has not been found on previous position");
        }

        if (animals.isEmpty()) {
            animalMap.remove(oldPosition);
        }

        place(animal);
    }
}

