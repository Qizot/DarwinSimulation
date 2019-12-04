package agh.cs.world;

import agh.cs.movement.LandBoundary;
import agh.cs.movement.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrasslandWatcher implements GrassPlanter  {

    private WorldMap map;
    private LandBoundary boundary;
    private List<LandBoundary> skipSpots;
    private int grassEnergy;

    private Random rand = new Random();

    public GrasslandWatcher(WorldMap map, LandBoundary boundary, List<LandBoundary> skipSpots, int grassEnergy) {
        this.map = map;
        this.boundary = boundary;
        this.skipSpots = skipSpots;
        this.grassEnergy = grassEnergy;
    }

    boolean mustBeSkipped(Vector2d pos) {
        return skipSpots.stream().anyMatch(ss -> ss.contains(pos));
    }

    public void plantGrass() {
        Vector2d lowerLeft = boundary.getLowerLeft();
        Vector2d upperRight = boundary.getUpperRight();

        List<Vector2d> emptyPlaces = new ArrayList<>();

        for (int x = lowerLeft.x; x <= upperRight.x; x++) {
            for (int y = lowerLeft.y; y <= upperRight.x; y++) {
                Vector2d slot = new Vector2d(x, y);
                if (!map.isOccupied(slot) && !mustBeSkipped(slot)) {
                    emptyPlaces.add(slot);
                }
            }
        }
        if (emptyPlaces.size() < 1) {
            return;
        }
        Vector2d emptyPlace = emptyPlaces.get(rand.nextInt(emptyPlaces.size()));
        map.place(new Grass(emptyPlace, grassEnergy));
    }



}
