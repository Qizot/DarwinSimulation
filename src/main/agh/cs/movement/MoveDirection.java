package agh.cs.movement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public enum MoveDirection {
    N(new Vector2d(0,1)),
    NE(new Vector2d(1,1)),
    NW(new Vector2d(-1,1)),
    E(new Vector2d(1,0)),
    W(new Vector2d(-1,0)),
    SE(new Vector2d(1,-1)),
    SW(new Vector2d(-1,-1)),
    S(new Vector2d(0,-1));

    Vector2d unitVector;
    static private Random rand = new Random();

    static private Map<Vector2d, MoveDirection> mappedDirections;
    static {
        mappedDirections = new LinkedHashMap<>();
        mappedDirections.put(new Vector2d(0, 1), MoveDirection.N);
        mappedDirections.put(new Vector2d(1,1), MoveDirection.NE);
        mappedDirections.put(new Vector2d(-1,1), MoveDirection.NW);
        mappedDirections.put(new Vector2d(1,0), MoveDirection.E);
        mappedDirections.put(new Vector2d(-1,0), MoveDirection.W);
        mappedDirections.put(new Vector2d(1,-1), MoveDirection.SE);
        mappedDirections.put(new Vector2d(-1,-1), MoveDirection.SW);
        mappedDirections.put(new Vector2d(0,-1), MoveDirection.S);
    }

    MoveDirection(Vector2d unitVector) {
        this.unitVector = unitVector;
    }

    static private double getVectorAngle(Vector2d vec) {
        return Math.atan2(vec.y, vec.x);
    }

    static private MoveDirection mapVectorToDirection(Vector2d vec) {
        MoveDirection dir = mappedDirections.get(vec);
        if (dir == null) {
            throw new IllegalArgumentException(String.format("given vector should be a unit vector, got %s instead", vec));
        }
        return dir;
    }

    static public MoveDirection getRandomDirection() {
        return MoveDirection.values()[rand.nextInt(8)];
    }

    public MoveDirection rotate(int rotates) {
        double r = 1;
        double currentAngle = getVectorAngle(this.unitVector);
        double newAngle = currentAngle + rotates * Math.PI / 4;

        int x = (int)Math.round(r * Math.cos(newAngle));
        int y = (int)Math.round(r * Math.sin(newAngle));
        return mapVectorToDirection(new Vector2d(x, y));
    }

    public Vector2d getUnitVector() {
        return unitVector;
    }
}
