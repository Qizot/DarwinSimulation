package agh.cs.world;

import agh.cs.animal.Animal;
import agh.cs.movement.IPositionChangeObserver;
import agh.cs.movement.Vector2d;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorldMap implements IPositionChangeObserver {


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
        elements.add(element);
    }

    private void removeElement(IMapElement element, Vector2d previous) {
        List<IMapElement> els = map.get(previous);
        if (els == null) {
            throw new IllegalArgumentException("element has not been found on position: " + previous);
        }
        els.remove(element);

        if (els.isEmpty()) {
            map.remove(previous);
        }
    }

    private void putElement(IMapElement element) {
        List<IMapElement> els = map.get(element.getPosition());

        if(els == null) {
            map.put(element.getPosition(), new ArrayList<IMapElement>() {{
                add(element);
            }});
            return;
        }

        // els is not empty so no matter what, non-movable element can't be placed there
        // if els contains movable element, current element can't be placed on top as well

        if (!element.isMovable()) {
            throw new IllegalArgumentException("element that is not movable cannot be placed on already occupied position");
        }

        if (els.stream().anyMatch(IMapElement::isMovable)) {
            throw new IllegalArgumentException("movable elements cannot be stacked on one another");

        }

        els.add(element);
    }

    public void run(MoveDirection[] directions) {
        IMapElement[] els = elements.stream().filter(IMapElement::isMovable).toArray(IMapElement[]::new);
        int MODULO = els.length;

        for (int i = 0; i < directions.length; i++) {
            IMapElement element = els[(i % MODULO)];
            element.move(directions[i]);
        }
    }

    public boolean isOccupied(Vector2d pos) {
        List<IMapElement> currentElements = map.get(pos);
        return !(currentElements == null);
    }

    public Object objectAt(Vector2d pos) {
        List<IMapElement> els = map.get(pos);

        if (els == null) return null;

        return els
                .stream()
                .filter(IMapElement::isMovable)
                .findFirst()
                .orElse(els.get(0));
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        List<IMapElement> els = map.get(oldPosition);
        if (els == null) {
            throw new IllegalArgumentException("no element has been found on position: " + oldPosition);
        }
        IMapElement element = els.stream().filter(IMapElement::isMovable).findFirst().orElse(null);

        if (element == null) {
            throw new IllegalArgumentException("no movable element has been found on position: " + oldPosition);
        }

        removeElement(element, oldPosition);
        putElement(element);
    }
}

