package agh.cs.animal;

import agh.cs.movement.Vector2d;
import agh.cs.world.WorldMap;

public class Breeder {

    static public Animal breed(Animal male, Animal female) {
        int entryEnergy = male.getEntryEnergy();

        if (male.getEnergy() / 2 < entryEnergy || female.getEnergy() / 2 < entryEnergy) {
            throw new IllegalArgumentException("either of parents doesn't have enough energy to make bebe");
        }

        WorldMap map = male.getMap();
        // child gets parent's position, getting empty one around might not be possible
        Vector2d position = male.getPosition();
        Genome genome = Genome.combineGenomes(male.getGenome(), female.getGenome());
        int energy = male.releaseEnergyForBaby() + female.releaseEnergyForBaby();

        return new Animal(map, position, genome, entryEnergy, energy);
    }
}
