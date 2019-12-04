package agh.cs;

import agh.cs.config.SimulationConfig;
import agh.cs.vizualization.AnimationMap;
import agh.cs.world.CycleController;
import agh.cs.world.SimulationCycleController;
import agh.cs.world.WorldMap;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DarwinSimulator extends  JPanel {

    private ScheduledExecutorService executor;
    private int animationTick = 50;
    private WorldMap map;
    private  SimulationConfig config;
    private CycleController controller;

    public DarwinSimulator() throws FileNotFoundException {
        config = SimulationConfig.loadConfigFromFile("/home/jakub/repos/DarwinSimulator/config.json");
        map = new WorldMap(config);
        controller = new SimulationCycleController(map);

        add(new AnimationMap(map));

//        animationTimer = new Timer(animationTick, new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//               controller.performCycle();
//                System.out.println("should repaint");
//               repaint();
//            }
//        });

        Runnable animate = new Runnable() {
            public void run() {
                controller.performCycle();
                repaint();
            }
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(animate, 0, animationTick, TimeUnit.MILLISECONDS);

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

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    static public void main(String[] args) throws FileNotFoundException {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() { createAndShowGUI();}
        });

    }
}
