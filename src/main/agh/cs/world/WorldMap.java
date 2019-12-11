package agh.cs.world;

import agh.cs.animal.Animal;
import agh.cs.config.SimulationConfig;
import agh.cs.movement.IPositionChangeObserver;
import agh.cs.movement.LandBoundary;
import agh.cs.movement.Vector2d;
import agh.cs.vizualization.MapVisualizable;
import agh.cs.vizualization.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements IPositionChangeObserver, MapVisualizable {

    private SimulationConfig config;
    private JungleWatcher jungleWatcher;
    private GrasslandWatcher grasslandWatcher;

    private LandBoundary boundary;

    private Map<Vector2d, List<Animal>> animalMap = new LinkedHashMap<>();
    private Map<Vector2d, Grass> grassMap = new LinkedHashMap<>();
    private Set<Vector2d> grassFreeSpots = new HashSet<>();

    public WorldMap(SimulationConfig config) {
        this.config = config;
        this.boundary = new LandBoundary(
            new Vector2d(0,0),
                new Vector2d(config.getWidth() - 1, config.getHeight() - 1)
        );

        LandBoundary jungleBoundary = generateJungleBoundaries();
        this.jungleWatcher = new JungleWatcher(this, jungleBoundary, config.getPlantEnergy());

        List<LandBoundary> skipLands = new ArrayList<>();
        skipLands.add(jungleBoundary);
        this.grasslandWatcher = new GrasslandWatcher(this, boundary, skipLands, config.getPlantEnergy());

        generateStartingAnimals();
        generateStartingPlants();
    }

    private void generateStartingAnimals() {
        Random rand = new Random();

        for (int i = 0; i < config.getStartAnimals(); i++) {
            int x = rand.nextInt(config.getWidth());
            int y = rand.nextInt(config.getHeight());
            new Animal(this, new Vector2d(x, y), config.getStartEnergy());
        }
    }

    private void generateStartingPlants() {

        List<Vector2d> occupied = getAnimals().stream().map(Animal::getPosition).collect(Collectors.toList());

        List<Vector2d> freeSlots = new ArrayList<>();
        for (int x = 0; x < config.getWidth(); x++) {
            for (int y = 0; y < config.getHeight(); y++) {
                grassFreeSpots.add(new Vector2d(x, y));
            }
        }

        grassFreeSpots.removeAll(occupied);


        int junglePlants = (int)Math.ceil(config.getJungleRatio() * config.getStartPlants());
        int grasslandPlants = config.getStartPlants() - junglePlants;

        this.jungleWatcher.plantGrass(junglePlants);
        this.grasslandWatcher.plantGrass(grasslandPlants);
    }

    private LandBoundary generateJungleBoundaries() {
        double ratio = this.config.getJungleRatio();
        if (ratio > 1.0) {
            throw new IllegalArgumentException("jungle ratio cannot be more than 1");
        }

        int jungleWidth = (int)Math.ceil(this.config.getWidth() * ratio);
        int jungleHeight = (int)Math.ceil(this.config.getHeight() * ratio);
        Vector2d lowerLeft = new Vector2d((this.config.getWidth() - jungleWidth)/2, (this.config.getHeight() - jungleHeight)/2);
        Vector2d upperRight = new Vector2d(lowerLeft.x + jungleWidth, lowerLeft.y + jungleHeight);
        return new LandBoundary(lowerLeft, upperRight);
    }

    public SimulationConfig getConfig() {
        return config;
    }

    public List<Animal> getAnimals() {
        return animalMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<Animal> getAnimalsAt(Vector2d pos) {
        return animalMap.get(pos);
    }

    public List<List<Animal>> getStackedAnimals() {
        return new ArrayList<>(animalMap.values());
    }

    public List<Grass> getGrasses() {
        return new ArrayList<>(grassMap.values());
    }

    public boolean canMoveTo(Vector2d pos) {
        return isInBoundaries(pos);
    }

    public Vector2d getReducedPosition(Vector2d pos) {
        int width = config.getWidth();
        int height = config.getHeight();
        return new Vector2d((pos.x + width) % width, (pos.y + height) % height);
    }

    private boolean isInBoundaries(Vector2d pos) {
        return boundary.getLowerLeft().precedes(pos) && boundary.getUpperRight().follows(pos);
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
        grassFreeSpots.remove(animal.getPosition());
    }

    // dunno if to throw exception or let it pass
    public void place(Grass grass) {
        if (!isOccupied(grass.getPosition())) {
            grassMap.put(grass.getPosition(), grass);
            grassFreeSpots.remove(grass.getPosition());
        }
    }

    public boolean isOccupied(Vector2d pos) {
        return (animalMap.get(pos) != null || grassMap.get(pos) != null);
    }

    public Object objectAt(Vector2d pos) {
        List<Animal> animals = animalMap.get(pos);
        if (animals == null || animals.isEmpty()) {
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
            grassFreeSpots.add(oldPosition);
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
            grassFreeSpots.add(animal.getPosition());
        }
    }

    void remove(Grass grass) {
        grassMap.remove(grass.getPosition());
    }

    void replantGrass() {
        this.jungleWatcher.plantGrass(1);
        this.grasslandWatcher.plantGrass(1);
    }

    public List<Vector2d> getFreeGrassSpots() {
        return grassFreeSpots.stream().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return new MapVisualizer(this).draw(boundary.getLowerLeft(), boundary.getUpperRight());
    }
}

