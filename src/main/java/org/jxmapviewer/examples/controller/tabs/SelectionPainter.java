package org.jxmapviewer.examples.controller.tabs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Neural Cortex
 */
public class SelectionPainter implements Painter<JXMapViewer> {

    private final Color fillColor = new Color(128, 192, 255, 128);
    private final Color frameColor = new Color(0, 0, 255, 128);
    private final Color circleFrameColor = Color.BLACK;
    private int selIndex = -1;

    private final SelectionAdapter adapter;
    private List<GeoPosition> list;

    public SelectionPainter(SelectionAdapter adapter, List<GeoPosition> list) {
        this.adapter = adapter;
        this.list = list;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
        // Enable antialiasing for smoother rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Rectangle rect = map.getViewportBounds();
        //g.translate(-rect.x, -rect.y);
        Rectangle rc = adapter.getRectangle();
        List<GeoPosition> selection = adapter.getSelection();

        if (rc != null) {
            g.setColor(frameColor);
            g.draw(rc);
            g.setColor(fillColor);
            g.fill(rc);
        }

        int radius = 5;

        for (GeoPosition geoPosition : list) {
            Point2D center = map.convertGeoPositionToPoint(geoPosition);
            g.setColor(Color.BLUE);
            if (selection.contains(geoPosition)) {
                for (int i = 0; i < selection.size(); i++) {
                    if (selIndex != -1 && selIndex == i) {
                        GeoPosition pos = selection.get(i);
                        drawCircleAroundSelected(g, map, pos, 10);
                        break;
                    }
                }
                 g.setColor(Color.RED);
            }
            g.fill(new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius));
        }
    }

    private void drawCircleAroundSelected(Graphics2D g, JXMapViewer map, GeoPosition selectedPos, int pixelRadius) {
        // Convert GeoPosition to screen point
        Point2D center = map.convertGeoPositionToPoint(selectedPos);

        // Ensure anti-aliasing is enabled
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw outline for better visibility
        g.setColor(circleFrameColor);
        g.draw(new Ellipse2D.Double(
                center.getX() - pixelRadius,
                center.getY() - pixelRadius,
                2 * pixelRadius,
                2 * pixelRadius
        ));
    }

    public void setList(List<GeoPosition> list) {
        this.list = list;
    }

    public void setSelIndex(int selIndex) {
        this.selIndex = selIndex;
    }
}
