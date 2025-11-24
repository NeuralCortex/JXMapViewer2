package org.jxmapviewer.examples.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.examples.adapter.RectangleAdapter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoRectangle;

/**
 * @author Neural Cortex
 */
public class RectanglePainter implements Painter<JXMapViewer> {
    
    private final RectangleAdapter adapter;
    private List<GeoRectangle> list;
    
    public RectanglePainter(RectangleAdapter adapter, List<GeoRectangle> list) {
        this.adapter = adapter;
        this.list = list;
    }
    
    @Override
    public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
        // Enable antialiasing for smoother rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Get the selected rectangle from the adapter
        GeoRectangle selectedRectangle = adapter.getSelectedRectangle();

        // Draw all non-selected rectangles first
        for (GeoRectangle rectangle : list) {
            if (rectangle != selectedRectangle) {
                // Convert GeoPositions to pixel coordinates
                Point2D bottomLeftPt = map.convertGeoPositionToPoint(rectangle.getBottomLeft());
                Point2D topRightPt = map.convertGeoPositionToPoint(rectangle.getTopRight());

                // Calculate rectangle dimensions in pixels
                int rectX = (int) bottomLeftPt.getX();
                int rectY = (int) topRightPt.getY(); // Top-right Y is smaller due to inverted Y-axis
                int rectWidth = (int) (topRightPt.getX() - bottomLeftPt.getX());
                int rectHeight = (int) (bottomLeftPt.getY() - topRightPt.getY());

                // Create AWT Rectangle
                Rectangle awtRectangle = new Rectangle(rectX, rectY, rectWidth, rectHeight);

                // Fill the rectangle with its color
                g.setColor(rectangle.getColor());
                g.fill(awtRectangle);
                
            }
        }

        // Draw the selected rectangle last (to bring it to the front)
        if (selectedRectangle != null) {
            // Convert GeoPositions to pixel coordinates
            Point2D bottomLeftPt = map.convertGeoPositionToPoint(selectedRectangle.getBottomLeft());
            Point2D topRightPt = map.convertGeoPositionToPoint(selectedRectangle.getTopRight());

            // Calculate rectangle dimensions in pixels
            int rectX = (int) bottomLeftPt.getX();
            int rectY = (int) topRightPt.getY();
            int rectWidth = (int) (topRightPt.getX() - bottomLeftPt.getX());
            int rectHeight = (int) (bottomLeftPt.getY() - topRightPt.getY());

            // Create AWT Rectangle
            Rectangle awtRectangle = new Rectangle(rectX, rectY, rectWidth, rectHeight);

            // Fill the rectangle with its color
            g.setColor(selectedRectangle.getColor());
            g.fill(awtRectangle);

            // Draw a red border
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.RED);
            g.draw(awtRectangle);
        }
    }
    
    public void setList(List<GeoRectangle> list) {
        this.list = list;
    }
}
