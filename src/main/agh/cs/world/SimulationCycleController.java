package agh.cs.world;

import agh.cs.animal.Animal;
import agh.cs.animal.Breeder;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimulationCycleController implements CycleController {


    private WorldMap map;
    private List<Animal> currentlyProcessedAnimals;

    public SimulationCycleController(WorldMap map) {
        this.map = map;
        setCurrentAnimals(map.getAnimals());
    }

    private void setCurrentAnimals(List<Animal> animals) {
        this.currentlyProcessedAnimals = animals;
    }



    @Override
    public void collectAnimalsEnergy() {
        int moveEnergy = map.getConfig().getMoveEnergy();
        currentlyProcessedAnimals.forEach(a -> a.takeEnergy(moveEnergy));
    }

    @Override
    public void removeDeadAnimals() {
        List<Animal> deadAnimals = currentlyProcessedAnimals
                .stream()
                .filter(Animal::isDead)
                .collect(Collectors.toList());
        deadAnimals.forEach(a -> map.remove(a));

        List<Animal> aliveAnimals = currentlyProcessedAnimals
                .stream()
                .filter(Predicate.not(Animal::isDead))
                .collect(Collectors.toList());
        setCurrentAnimals(aliveAnimals);
    }

    @Override
    public void rotateAnimals() {
        currentlyProcessedAnimals.forEach(Animal::rotate);
    }

    @Override
    public void moveAnimals() {
        currentlyProcessedAnimals.forEach(Animal::move);
    }

    private void feedSingleGrass(Grass grass) {

        List<Animal> animals = map.getAnimalsAt(grass.getPosition());
        if (animals == null) return;

        animals.sort((a1, a2) -> Integer.compare(a2.getEnergy(), a1.getEnergy()));

        int maxEnergy = animals.get(0).getEnergy();
        int candidates = (int)animals.stream().filter(a -> a.getEnergy() == maxEnergy).count();

        for (int i = 0; i < candidates; i++) {
            animals.get(i).feedEnergy(grass.getEnergy() / candidates);
        }

        map.remove(grass);
    }

    @Override
    public void feedAnimals() {
        map.getGrasses().forEach(this::feedSingleGrass);
    }

    @Override
    public void replantGrass() {
        map.replantGrass();
    }

    private void breedStackedAnimals(List<Animal> animals) {
        if (animals.size() < 2) return;
        animals.sort((a1, a2) -> Integer.compare(a2.getEnergy(),  a1.getEnergy()));
        List<Animal> parents = animals.subList(0, 2);

        try {
            Breeder.breed(parents.get(0), parents.get(1));
        } catch (IllegalArgumentException e) {
            // they simply dont have enough energy
        }
    }

    @Override
    public void breedAnimals() {
        map.getStackedAnimals().forEach(this::breedStackedAnimals);
    }

    @Override
    public void performCycle() {
        setCurrentAnimals(map.getAnimals());
        collectAnimalsEnergy();
        removeDeadAnimals();
        rotateAnimals();
        moveAnimals();
        feedAnimals();
        breedAnimals();
        replantGrass();
    }

    @Override
    public int animalsAfterCycle() {
        return currentlyProcessedAnimals.size();
    }
}
