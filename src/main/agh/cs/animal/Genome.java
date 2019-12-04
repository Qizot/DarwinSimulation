package agh.cs.animal;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    static private Random rand = new Random();
    private static int genNum = 32;
    private List<Integer> genes;

    private Genome(List<Integer> genes) {
        this.genes = genes;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    static public Genome generateNewGenome() {
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < genNum; i++) {
            genes.add(rand.nextInt(8));
        }
        genes.sort(Integer::compare);
        return new Genome(genes);
    }

    static public Genome combineGenomes(Genome first, Genome second) {
        List<Integer> newGenes = new ArrayList<>();

        // search for space between elements up to which copy elements: e.g. _ | _ | _ _ | _
        // if we have n elements, there are n - 1 walls, then we copy elements till idx < wall_idx
        int firstCut = rand.nextInt(genNum - 1) + 1;
        int secondCut = rand.nextInt(genNum - 1) + 1;

        // make sure cuts are not the same as so not to create empty genes parts
        while (firstCut == secondCut) {
            secondCut = rand.nextInt(genNum - 1) + 1;
        }

        int i = 0;
        List<Integer> firstGenes = first.getGenes();
        List<Integer> secondGenes = second.getGenes();

        while (i < firstCut) {
            newGenes.add(firstGenes.get(i));
            i++;
        }

        while (i < secondCut) {
            newGenes.add(firstGenes.get(i));
            i++;
        }

        //
        while (i < genNum) {
            newGenes.add(firstGenes.get(i));
            i++;
        }

        newGenes.sort(Integer::compare);
        return new Genome(newGenes);
    }
}
