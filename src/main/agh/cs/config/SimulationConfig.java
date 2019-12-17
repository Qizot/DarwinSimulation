package agh.cs.config;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class SimulationConfig {
    private int width;
    private int height;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;
    private int startAnimals;
    private int startPlants;
    private int animationSpeed;
    private boolean showChart;

    static public SimulationConfig loadConfigFromFile(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        SimulationConfig cfg = (SimulationConfig)gson.fromJson(new FileReader(filename), SimulationConfig.class);
        cfg.validate();
        return cfg;
    }

    public void validate() {
        if (width <= 0)
            throw new IllegalArgumentException("width must be a positive integer");
        if (height <= 0)
            throw new IllegalArgumentException("width must be a positive integer");
        if (startEnergy < 0)
            throw new IllegalArgumentException("start energy must be non-negative");
        if (jungleRatio <= 0.0 || jungleRatio >= 1.0)
            throw new IllegalArgumentException("jungleRatio must be within range (0, 1)");
        if (startAnimals <= 0)
            throw new IllegalArgumentException("startAnimals must be non-negative");
        if (startPlants <= 0)
            throw new IllegalArgumentException("startPlants must be non-negative");
        if (animationSpeed <= 0)
            throw new IllegalArgumentException("animationSpeed must be a positive integer");
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public void setMoveEnergy(int moveEnergy) {
        this.moveEnergy = moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public void setPlantEnergy(int plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    public double getJungleRatio() {
        return jungleRatio;
    }

    public void setJungleRatio(double jungleRatio) {
        this.jungleRatio = jungleRatio;
    }

    public int getStartAnimals() {
        return startAnimals;
    }

    public void setStartAnimals(int startAnimals) {
        this.startAnimals = startAnimals;
    }

    public int getStartPlants() {
        return startPlants;
    }

    public void setStartPlants(int startPlants) {
        this.startPlants = startPlants;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public boolean getShowChart() {
        return showChart;
    }

    public void setShowChart(boolean showChart) {
        this.showChart = showChart;
    }
}
