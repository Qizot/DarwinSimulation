package agh.cs;

import agh.cs.config.SimulationConfig;
import agh.cs.vizualization.AnimationMap;
import agh.cs.world.CycleController;
import agh.cs.world.SimulationCycleController;
import agh.cs.world.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DarwinSimulator extends  JPanel {

    private ScheduledExecutorService executor;
    private int animationTick = 16;
    private WorldMap map;
    private  SimulationConfig config;
    private CycleController controller;
    private JLabel animalsLabel = new JLabel();
    private JLabel grassLabel = new JLabel();

    public DarwinSimulator() throws FileNotFoundException {
        config = SimulationConfig.loadConfigFromFile("/home/jakub/repos/DarwinSimulator/config.json");
        map = new WorldMap(config);
        System.out.println("Initialized map");
        controller = new SimulationCycleController(map);
        System.out.println("Initialized simulation controller");
        add(new AnimationMap(map));

        Box box = Box.createVerticalBox();
        box.add(animalsLabel);
        box.add(grassLabel);
        add(box, BorderLayout.NORTH);
        animalsLabel.setPreferredSize(new Dimension(100,40));

        grassLabel.setPreferredSize(new Dimension(100,40));

        Runnable animate = new Runnable() {
            public void run() {
                controller.performCycle();
                animalsLabel.setText("Animals: " + Integer.toString(controller.animalsAfterCycle()));
                grassLabel.setText("Grass: " + Integer.toString(map.getGrasses().size()));
                repaint();
            }
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(animate, 0, animationTick, TimeUnit.MILLISECONDS);
        System.out.println("Finished setting up...");

    }

    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("DarwinSimulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            JComponent newContentPane = new DarwinSimulator();
            frame.setContentPane(newContentPane);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        frame.pack();
        frame.setVisible(true);
    }

    static public void main(String[] args) throws FileNotFoundException {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() { createAndShowGUI();}
        });

    }
}
