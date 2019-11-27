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

    static public SimulationConfig loadConfigFromFile(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        return (SimulationConfig)gson.fromJson(new FileReader(filename), SimulationConfig.class);
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
}
