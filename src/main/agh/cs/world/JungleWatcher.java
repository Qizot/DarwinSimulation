package agh.cs.world;

import agh.cs.movement.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JungleWatcher {
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private WorldMap map;
    private Random rand = new Random();

    public JungleWatcher(WorldMap map, Vector2d lowerLeft, Vector2d upperRight) {
        this.map = map;
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    public boolean isInsideJungle(Vector2d pos) {
        return lowerLeft.precedes(pos) && upperRight.follows(pos);
    }

    public void plantRandomGrass() {
        List<Vector2d> emptyPlaces = new ArrayList<>();

        for (int x = lowerLeft.x; x < upperRight.x; x++) {
            for (int y = lowerLeft.y; y < upperRight.x; y++) {
                Vector2d slot = new Vector2d(x, y);
                if (!map.isOccupied(slot)) {
                    emptyPlaces.add(slot);
                }
            }
        }
        Vector2d emptyPlace = emptyPlaces.get(rand.nextInt(emptyPlaces.size()));
        map.place(new Grass(emptyPlace));
    }
}
