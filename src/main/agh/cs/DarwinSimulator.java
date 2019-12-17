package agh.cs;

import agh.cs.config.SimulationConfig;
import agh.cs.vizualization.AnimationMap;
import agh.cs.vizualization.PopulationChart;
import agh.cs.world.CycleController;
import agh.cs.world.SimulationCycleController;
import agh.cs.world.WorldMap;
import org.knowm.xchart.XChartPanel;

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
    private PopulationChart chart;
    private JLabel animalsLabel = new JLabel();
    private JLabel grassLabel = new JLabel();

    private long lastExecution = System.currentTimeMillis();

    public DarwinSimulator() throws FileNotFoundException {
        config = SimulationConfig.loadConfigFromFile("/home/jakub/repos/DarwinSimulator/config.json");
        map = new WorldMap(config);
        controller = new SimulationCycleController(map);

        if (config.getShowChart()) {
            chart = new PopulationChart();
        }

        this.animationTick = config.getAnimationSpeed();

        AnimationMap animationMap = new AnimationMap(map);

        animalsLabel.setOpaque(true);
        grassLabel.setOpaque(true);

        Box box = Box.createVerticalBox();
        box.add(animalsLabel);
        box.add(grassLabel);


        add(box);
        add(animationMap);
        if (chart != null)
            chart.go();

        animalsLabel.setPreferredSize(new Dimension(120,40));
        grassLabel.setPreferredSize(new Dimension(120,40));

        Runnable animate = new Runnable() {
            public void run() {
                long startTime = System.currentTimeMillis();

                controller.performCycle();
                int animals = controller.animalsAfterCycle();
                int grass = map.getGrasses().size();
                if (chart != null)
                    chart.addNewValues(animals, grass);
                animalsLabel.setText("Animals: " + Integer.toString(animals));
                grassLabel.setText("Grass: " + Integer.toString(grass));
                repaint();

//                long stopTime = System.currentTimeMillis();
//                long elapsedTime = stopTime - startTime;
//                System.out.println("Frame rendered in " + elapsedTime);
//                System.out.println("Delay between frames " + (startTime - lastExecution));
//                lastExecution = stopTime;
            }
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(animate, 0, animationTick, TimeUnit.MILLISECONDS);
        System.out.println("Finished setting up...");

    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(false);

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
