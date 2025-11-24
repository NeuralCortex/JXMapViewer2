package org.jxmapviewer.examples.adapter;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * @author Neural Cortex
 */
public class SelectionAdapter extends MouseAdapter {

    private boolean dragging;
    private final JXMapViewer viewer;
    private List<GeoPosition> list;
    private GeoPosition selectionTopLeft; // Store selection as GeoPositions
    private GeoPosition selectionBottomRight;

    private Point2D startPos = new Point2D.Double();
    private Point2D endPos = new Point2D.Double();
    
    public interface SelectionAdapterListener{
        public void getSelection(List<GeoPosition> sel);
    }
    private SelectionAdapterListener selectionAdapterListener;

    public SelectionAdapter(JXMapViewer viewer, List<GeoPosition> list) {
        this.viewer = viewer;
        this.list = list;
        this.selectionTopLeft = null;
        this.selectionBottomRight = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON3) {
            return;
        }

        startPos.setLocation(e.getX(), e.getY());
        endPos.setLocation(e.getX(), e.getY());
        selectionTopLeft = null; // Reset selection
        selectionBottomRight = null;

        dragging = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!dragging) {
            return;
        }

        endPos.setLocation(e.getX(), e.getY());
        viewer.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!dragging) {
            return;
        }

        if (e.getButton() != MouseEvent.BUTTON3) {
            return;
        }

        // Convert pixel coordinates to GeoPositions when selection is finalized
        selectionTopLeft = viewer.convertPointToGeoPosition(startPos);
        selectionBottomRight = viewer.convertPointToGeoPosition(endPos);
        
        selectionAdapterListener.getSelection(getSelection());

        viewer.repaint();
        dragging = false;
    }

    public Rectangle getRectangle() {
        if (dragging) {
            int x1 = (int) Math.min(startPos.getX(), endPos.getX());
            int y1 = (int) Math.min(startPos.getY(), endPos.getY());
            int x2 = (int) Math.max(startPos.getX(), endPos.getX());
            int y2 = (int) Math.max(startPos.getY(), endPos.getY());

            return new Rectangle(x1, y1, x2 - x1, y2 - y1);
        } 
        return null;
    }

    public List<GeoPosition> getSelection() {
        if (selectionTopLeft == null || selectionBottomRight == null) {
            return new ArrayList<>(); // No selection
        }

        List<GeoPosition> selected = new ArrayList<>();

        // Use stored GeoPositions for selection
        double minLat = Math.min(selectionTopLeft.getLatitude(), selectionBottomRight.getLatitude());
        double maxLat = Math.max(selectionTopLeft.getLatitude(), selectionBottomRight.getLatitude());
        double minLon = Math.min(selectionTopLeft.getLongitude(), selectionBottomRight.getLongitude());
        double maxLon = Math.max(selectionTopLeft.getLongitude(), selectionBottomRight.getLongitude());

        for (GeoPosition pos : list) {
            double lat = pos.getLatitude();
            double lon = pos.getLongitude();
            if (lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon) {
                selected.add(pos);
            }
        }
        return selected;
    }

    public void setList(List<GeoPosition> list) {
        this.list = list;
        // Reset selection when the list changes
        selectionTopLeft = null;
        selectionBottomRight = null;
        startPos = new Point2D.Double();
        endPos = new Point2D.Double();
    }

    public void setSelectionAdapterListener(SelectionAdapterListener selectionAdapterListener) {
        this.selectionAdapterListener = selectionAdapterListener;
    }
}
