package agh.cs.vizualization;

import agh.cs.movement.Vector2d;
import agh.cs.world.WorldMap;

import javax.swing.*;
import java.awt.*;

public class AnimationMap extends JPanel {

    private WorldMap map;
    private int x;
    private int y;
    public int PREFERRED_GRID_SIZE_PIXELS = 5;


    public AnimationMap(WorldMap map) {
        this.map = map;
        this.x = map.getConfig().getWidth();
        this.y = map.getConfig().getHeight();
        this.PREFERRED_GRID_SIZE_PIXELS = 800 / x;

        int preferredWidth = x * PREFERRED_GRID_SIZE_PIXELS;
        int preferredHeight = y * PREFERRED_GRID_SIZE_PIXELS;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the grid
        int rectWidth = getWidth() / x;
        int rectHeight = getHeight() / y;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                // Upper left corner of this terrain rect
                int x_pos = i * rectWidth;
                int y_pos = j * rectHeight;
                Object o = map.objectAt(new Vector2d(i, j));
                Color color = ColorInterpolator.getColor(o);
                g.setColor(color);
                g.fillRect(x_pos, y_pos, rectWidth, rectHeight);
            }
        }
    }
}
