package org.jxmapviewer.examples.controller.tabs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;

/**
 *
 * @author Neural Cortex
 */
public class CenterPainter implements Painter<JXMapViewer> {

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
        g = (Graphics2D) g.create();

        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));

        drawCenter(g, map);

        g.dispose();
    }

    private void drawCenter(Graphics2D g, JXMapViewer map) {
        // Enable antialiasing for smoother rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Get the current map center in tile pixel coordinates
        Point2D center = map.getCenter();

        // Draw a red filled circle (point) at the center, 10px radius
        int radius = 10;
        g.setColor(Color.RED);
        g.fill(new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius));
    }
}
