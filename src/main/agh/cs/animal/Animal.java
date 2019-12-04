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
    private Random rand = new Random();

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

    public Vector2d getPosition() {
        return position;
    }

    public MoveDirection getDirection() { return direction; }

    @Override
    public String toString() {
        return "A";
    }



    public boolean isDead() {
        return energy <= 0;
    }

    public int getEntryEnergy() {
        return entryEnergy;
    }

    public int getEnergy() {
        return energy;
    }

    public void feedEnergy(int energyPortion) {
        this.energy += energyPortion;
    }

    public void takeEnergy(int energyPortion) {
        this.energy -= energyPortion;
    }

    WorldMap getMap() {
        return map;
    }







    public int releaseEnergyForBaby() {
        int babyEnergy = energy / 4;
        energy -= babyEnergy;
        return babyEnergy;
    }

    private int getNextRotation () {
        return genome.getGenes().get(rand.nextInt(32));
    }

    public void rotate() {
        this.direction = direction.rotate(getNextRotation());
    }

    public void move() {
        // bad, bad design, animal shouldn't be responsible for remembering that
        Vector2d newPosition = this.map.getReducedPosition(position.add(direction.getUnitVector()));
        if(map.canMoveTo(newPosition)) {
            Vector2d old = position;
            position = newPosition;
            map.positionChanged(this, old);
        }
    }
}
