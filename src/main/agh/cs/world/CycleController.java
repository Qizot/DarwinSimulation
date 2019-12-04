package agh.cs.world;

public interface CycleController {
    void collectAnimalsEnergy();
    void removeDeadAnimals();
    void rotateAnimals();
    void moveAnimals();
    void feedAnimals();
    void replantGrass();
    void breedAnimals();
    void performCycle();
    int animalsAfterCycle();
}
