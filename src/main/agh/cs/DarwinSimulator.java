package agh.cs;

import agh.cs.config.SimulationConfig;

import java.io.FileNotFoundException;

public class DarwinSimulator {
    static public void main(String[] args) throws FileNotFoundException {
        SimulationConfig config = SimulationConfig.loadConfigFromFile("/home/jakub/repos/DarwinSimulator/config.json");
        System.out.println(config.getWidth());
    }
}
