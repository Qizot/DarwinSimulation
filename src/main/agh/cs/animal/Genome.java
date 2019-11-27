package agh.cs.animal;

import java.util.Random;

public class Genome {
    static private Random rand = new Random();
    private static int genNum = 32;
    private int[] genes;

    private Genome(int[] genes) {
        this.genes = genes;
    }

    public int[] getGenes() {
        return genes;
    }

    static public Genome generateNewGenome() {
        int[] genes = new int[genNum];
        for (int i = 0; i < genNum; i++) {
            genes[i] = rand.nextInt(8);
        }
        return new Genome(genes);
    }

    static public Genome combineGenomes(Genome first, Genome second) {
        int[] newGenes = new int[genNum];

        // search for space between elements up to which copy elements: e.g. _ | _ | _ _ | _
        // if we have n elements, there are n - 1 walls, then we copy elements till idx < wall_idx
        int firstCut = rand.nextInt(genNum - 1) + 1;
        int secondCut = rand.nextInt(genNum - 1) + 1;

        // make sure cuts are not the same as so not to create empty genes parts
        while (firstCut == secondCut) {
            secondCut = rand.nextInt(genNum - 1) + 1;
        }

        int i = 0;
        int[] firstGenes = first.getGenes();
        int[] secondGenes = second.getGenes();

        while (i < firstCut) {
            newGenes[i] = firstGenes[i];
            i++;
        }

        while (i < secondCut) {
            newGenes[i] = secondGenes[i];
            i++;
        }

        //
        while (i < genNum) {
            newGenes[i] = firstGenes[i];
            i++;
        }

        return new Genome(newGenes);
    }
}
