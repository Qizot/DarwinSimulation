package agh.cs.vizualization;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class PopulationChart {
    PopulationWorker populationWorker;
    SwingWrapper<XYChart> sw;
    XYChart chart;

    public void addNewValues(int animals, int grass) {
        populationWorker.addNewValue("Animals", animals);
        populationWorker.addNewValue("Grass", grass);
    }

    public XYChart getChart() {
        return chart;
    }

    public void go() {

        // Create Chart

        chart = new XYChartBuilder().width(800).height(600).title("Population Chart").xAxisTitle("X").yAxisTitle("Units").build();

        chart.getStyler().setXAxisTicksVisible(false);
        chart.getStyler().setXAxisTitleVisible(false);

        // Series
        chart.addSeries("Grass", new double[] { 0 }, new double[] { 0}).setMarker(SeriesMarkers.NONE);
        chart.addSeries("Animals", new double[] { 0 }, new double[] { 0 }).setMarker(SeriesMarkers.NONE);


        sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        populationWorker = new PopulationWorker();
        populationWorker.execute();
    }

    private class PopulationWorker extends SwingWorker<Boolean, Integer[][]> {

        private Semaphore mutex = new Semaphore(1);
        List<Integer> animalsValues = new LinkedList<>();
        List<Integer> grassValues = new LinkedList<>();
        private int repaintTick = 200;
        private int queueSize = 100;


        public PopulationWorker() {

        }

        public void addNewValue(String seriesName, Integer value) {

            try {
                mutex.acquire();
                if (seriesName.equals("Animals")) {
                    animalsValues.add(value);
                } else if (seriesName.equals("Grass")) {
                    grassValues.add(value);
                }

            } catch (InterruptedException e) {

            } finally {
                mutex.release();
            }

        }

        @Override
        protected Boolean doInBackground() throws Exception {

            while (!isCancelled()) {

                try {
                    mutex.acquire();
                    Integer[] animals = animalsValues.stream().toArray(Integer[]::new);
                    Integer[] grass = grassValues.stream().toArray(Integer[]::new);
                    Integer[][] series = new Integer[2][];
                    series[0] = animals;
                    series[1] = grass;
                    publish(series);

                } catch (InterruptedException e) {
                    System.out.println("error while doing task in the background");
                } finally {
                    mutex.release();
                }

                try {
                    Thread.sleep(repaintTick);
                } catch (InterruptedException e) {
                    // eat it. caught when interrupt is called
                    System.out.println("MySwingWorker shut down.");
                }

            }

            return true;
        }

        @Override
        protected void process(List<Integer[][]> series) {

            series.forEach(serie -> {
                chart.updateXYSeries("Animals", null, Arrays.asList(serie[0]), null);
                chart.updateXYSeries("Grass", null, Arrays.asList(serie[1]), null);

            });

            sw.repaintChart();

            long start = System.currentTimeMillis();

            long duration = System.currentTimeMillis() - start;
            try {
                Thread.sleep(100 - duration); // 40 ms ==> 25fps
                // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
            } catch (InterruptedException e) {
            }

        }
    }
}


