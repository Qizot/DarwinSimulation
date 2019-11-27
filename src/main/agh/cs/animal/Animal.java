package agh.cs.animal;

import agh.cs.movement.MoveDirection;
import agh.cs.movement.Vector2d;
import agh.cs.world.WorldMap;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

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

    public Vector2d getPosition() {
        return position;
    }

    public int releaseEnergyForBaby() {
        int babyEnergy = energy / 4;
        energy -= babyEnergy;
        return babyEnergy;
    }

    private int[] calculateRotatePossibilities() {
        List<Integer> genes = Arrays.stream(genome.getGenes()).boxed().collect(Collectors.toList());
        List<Pair<Integer, Integer>> rotates = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rotates.add(new Pair<Integer, Integer>(i, Collections.frequency(genes, i)));
        }
        Collections.sort(rotates, Comparator.comparing(p -> -p.getValue()));

        return rotates.stream().map(Pair::getKey).mapToInt(i->i).toArray();
    }



    @Override
    public String toString() {
        return "A";
    }

    public MoveDirection getDirection() { return direction; }

    public void move() {

    }
}
