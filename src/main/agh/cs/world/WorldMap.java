package agh.cs.world;

import agh.cs.animal.Animal;
import agh.cs.movement.IPositionChangeObserver;
import agh.cs.movement.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements IPositionChangeObserver {

    private int width;
    private int height;
    private Vector2d lowerLeft;
    private Vector2d upperRight;

    private Map<Vector2d, List<Animal>> animalMap = new LinkedHashMap<>();
    private Map<Vector2d, Grass> grassMap = new LinkedHashMap<>();

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0,0);
        this.upperRight = new Vector2d(width - 1, height - 1);
    }

    public boolean canMoveTo(Vector2d pos) {
        return isInBoundaries(pos);
    }

    public Vector2d getReducedPosition(Vector2d pos) {
        return new Vector2d((pos.x + width) % width, (pos.y + height) % height);
    }

    private boolean isInBoundaries(Vector2d pos) {
        return lowerLeft.precedes(pos) && upperRight.follows(pos);
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

    // dunno if to throw exception or let it pass
    public void place(Grass grass) {
        if (!isOccupied(grass.getPosition())) {
            grassMap.put(grass.getPosition(), grass);
        }
    }

    public boolean isOccupied(Vector2d pos) {
        return false;
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

        if (!isInBoundaries(animal.getPosition())) {
            throw new IllegalArgumentException("animal has been moved to forbidden position");
        }

        if (animals.isEmpty()) {
            animalMap.remove(oldPosition);
        }

        place(animal);
    }

    void remove(Animal animal) {
        List<Animal> animals = animalMap.get(animal.getPosition());
        if (animals == null) {
            throw new IllegalArgumentException("animal has not been found on the map");
        }

        animals.remove(animal);
        if (animals.isEmpty()) {
            animalMap.remove(animal.getPosition());
        }
    }

    void remove(Grass grass) {
        grassMap.remove(grass.getPosition());
    }

    void removeDeadAnimals() {
       animalMap.values().stream()
                .flatMap(Collection::stream)
                .filter(Animal::isDead)
                .forEach(this::remove);
    }

    void rotateAnimals() {
        animalMap.values().stream()
                .flatMap(Collection::stream)
                .forEach(Animal::rotate);
    }

    public void simulateNextCycle() {
        removeDeadAnimals();
    }
}

