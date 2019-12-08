package agh.cs.world;

import agh.cs.movement.LandBoundary;
import agh.cs.movement.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JungleWatcher implements GrassPlanter {

    private WorldMap map;
    private LandBoundary boundary;
    private int grassEnergy;

    private Random rand = new Random();

    public JungleWatcher(WorldMap map, LandBoundary boundary, int grassEnergy) {
        this.map = map;
        this.boundary = boundary;
        this.grassEnergy = grassEnergy;
        System.out.println("jungle boundary: " + boundary);
    }

    public boolean isInsideJungle(Vector2d pos) {
        return boundary.getLowerLeft().precedes(pos) && boundary.getUpperRight().follows(pos);
    }

    public void plantGrass(int n) {
        Vector2d lowerLeft = boundary.getLowerLeft();
        Vector2d upperRight = boundary.getUpperRight();

        List<Vector2d> emptyPlaces = new ArrayList<>();

        for (int x = lowerLeft.x; x <= upperRight.x; x++) {
            for (int y = lowerLeft.y; y <= upperRight.y; y++) {
                Vector2d slot = new Vector2d(x, y);
                if (!map.isOccupied(slot)) {
                    emptyPlaces.add(slot);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (emptyPlaces.size() < 1) return;
            Vector2d emptyPlace = emptyPlaces.get(rand.nextInt(emptyPlaces.size()));
            emptyPlaces.remove(emptyPlace);
            map.place(new Grass(emptyPlace, grassEnergy));
        }
    }
}
