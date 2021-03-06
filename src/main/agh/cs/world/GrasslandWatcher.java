package agh.cs.world;

import agh.cs.movement.LandBoundary;
import agh.cs.movement.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public void plantGrass(int n) {
        Vector2d lowerLeft = boundary.getLowerLeft();
        Vector2d upperRight = boundary.getUpperRight();

        List<Vector2d> emptyPlaces = map.getFreeGrassSpots()
                .stream()
                .filter(v -> lowerLeft.precedes(v) && upperRight.follows(v) && !mustBeSkipped(v))
                .collect(Collectors.toList());

        for (int i = 0; i < n; i++) {
            if (emptyPlaces.size() < 1) return;
            Vector2d emptyPlace = emptyPlaces.get(rand.nextInt(emptyPlaces.size()));
            emptyPlaces.remove(emptyPlace);
            map.place(new Grass(emptyPlace, grassEnergy));
        }
    }



}
