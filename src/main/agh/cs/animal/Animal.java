package agh.cs.animal;

import agh.cs.movement.MoveDirection;
import agh.cs.movement.Vector2d;
import agh.cs.world.WorldMap;
import javafx.util.Pair;
import java.util.*;

public class Animal {
    private WorldMap map;
    private Vector2d position;
    private MoveDirection direction;
    private Genome genome;
    private final int entryEnergy;
    private int energy;

    public Animal(WorldMap map, Vector2d position, int entryEnergy) {
        this.map = map;
        this.position = position;
        this.direction = MoveDirection.getRandomDirection();
        this.genome = Genome.generateNewGenome();
        this.entryEnergy = entryEnergy;
        this.energy = entryEnergy;

        map.place(this);
    }

    // baby constructor
    public Animal(WorldMap map, Vector2d position, Genome genome, int entryEnergy, int energy) {
        this.map = map;
        this.position = position;
        this.direction = MoveDirection.getRandomDirection();
        this.genome = genome;
        this.entryEnergy = entryEnergy;
        this.energy = energy;

        map.place(this);
    }

    public Genome getGenome() {
        return genome;
    }

    WorldMap getMap() {
        return map;
    }

    int getEnergy() {
        return energy;
    }

    int getEntryEnergy() {
        return entryEnergy;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int releaseEnergyForBaby() {
        int babyEnergy = energy / 4;
        energy -= babyEnergy;
        return babyEnergy;
    }

    private int getNextRotation () {
        List<Integer> genes = genome.getGenes();
        List<Pair<Integer, Integer>> rotates = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rotates.add(new Pair<Integer, Integer>(i, Collections.frequency(genes, i)));
        }
        rotates.sort(Comparator.comparing(p -> -p.getValue()));

        return rotates.stream().map(Pair::getKey).mapToInt(i->i).findFirst().orElseThrow();
    }



    @Override
    public String toString() {
        return "A";
    }

    public MoveDirection getDirection() { return direction; }

    // rotate should not be included in move
    public void move() {
        this.direction = direction.rotate(getNextRotation());
        Vector2d newPosition = this.position.add(direction.getUnitVector());
        if(map.canMoveTo(newPosition)) {
            Vector2d old = position;
            position = newPosition;
            map.positionChanged(this, old);
        }
    }
}
