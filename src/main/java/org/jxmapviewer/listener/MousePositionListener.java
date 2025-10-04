package org.jxmapviewer.listener;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Neural Cortex
 */
public class MousePositionListener implements MouseMotionListener {

    private final JXMapViewer mapViewer;

    public interface GeoPosListener {

        public void getGeoPos(GeoPosition geoPosition);
    }

    private GeoPosListener geoPosListener;

    public MousePositionListener(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Rectangle rect = mapViewer.getViewportBounds();
        double x = rect.getX() + e.getX();
        double y = rect.getY() + e.getY();

        GeoPosition geoPosition = mapViewer.getTileFactory().pixelToGeo(new Point((int) x, (int) y), mapViewer.getZoom());
        geoPosListener.getGeoPos(geoPosition);
    }

    public void setGeoPosListener(GeoPosListener geoPosListener) {
        this.geoPosListener = geoPosListener;
    }
}
