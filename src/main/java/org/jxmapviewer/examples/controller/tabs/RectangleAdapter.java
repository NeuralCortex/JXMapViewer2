package org.jxmapviewer.examples.controller.tabs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.GeoRectangle;

/**
 * @author Neural Cortex
 */
public class RectangleAdapter extends MouseAdapter {

    private final JXMapViewer viewer;
    private List<GeoRectangle> list;
    private GeoRectangle selectedRectangle;
    private int selectedIndex;

    public RectangleAdapter(JXMapViewer viewer, List<GeoRectangle> list) {
        this.viewer = viewer;
        this.list = list;
        this.selectedRectangle = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Convert mouse click pixel coordinates to GeoPosition
        Point2D clickPoint = e.getPoint();
        GeoPosition clickGeoPos = viewer.convertPointToGeoPosition(clickPoint);

        // Check if the click is within any rectangle
        selectedRectangle = null; // Reset selection
        for (int i = 0; i < list.size(); i++) {
            GeoRectangle rectangle = list.get(i);
            GeoPosition bottomLeft = rectangle.getBottomLeft();
            GeoPosition topRight = rectangle.getTopRight();

            double clickLat = clickGeoPos.getLatitude();
            double clickLon = clickGeoPos.getLongitude();
            double minLat = Math.min(bottomLeft.getLatitude(), topRight.getLatitude());
            double maxLat = Math.max(bottomLeft.getLatitude(), topRight.getLatitude());
            double minLon = Math.min(bottomLeft.getLongitude(), topRight.getLongitude());
            double maxLon = Math.max(bottomLeft.getLongitude(), topRight.getLongitude());

            // Check if click is within the rectangle's bounds
            if (clickLat >= minLat && clickLat <= maxLat
                    && clickLon >= minLon && clickLon <= maxLon) {

                selectedIndex = i;

                break; // Select the first rectangle found
            }
        }

        // Repaint the map to update visual indication (e.g., highlight selected rectangle)
        viewer.repaint();
    }

    public GeoRectangle getSelectedRectangle() {
        return list.get(selectedIndex);
    }

    public void setSelectedRectangle(GeoRectangle selectedRectangle) {
        this.selectedRectangle = selectedRectangle;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void setList(List<GeoRectangle> list) {
        this.list = list;
    }
}
