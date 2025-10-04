package org.jxmapviewer.viewer;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapViewer;

/**
 * A standard waypoint renderer that supports scaling the waypoint image by a
 * percentage.
 *
 * @author joshy
 */
public class DefaultWaypointRenderer implements WaypointRenderer<Waypoint> {

    private static final Logger log = LogManager.getLogger(DefaultWaypointRenderer.class);

    private BufferedImage img = null;
    private double scalePercent = 100.0; // Default to 100% (original size)

    /**
     * Uses a default waypoint image with default size (100%).
     */
    public DefaultWaypointRenderer() {
        this(100.0); // Call parameterized constructor with default scale
    }

    /**
     * Uses a default waypoint image with a specified scale percentage.
     *
     * @param scalePercent The percentage to scale the image (e.g., 50.0 for
     * 50%).
     */
    public DefaultWaypointRenderer(double scalePercent) {
        if (scalePercent <= 0) {
            throw new IllegalArgumentException("Scale percentage must be greater than 0");
        }
        this.scalePercent = scalePercent;
        try {
            img = ImageIO.read(DefaultWaypointRenderer.class.getResource(Globals.PATH_IMG_WAYPOINT));
        } catch (Exception ex) {
            log.warn("Couldn't read " + Globals.PATH_IMG_WAYPOINT, ex);
        }
    }

    /**
     * Sets the scale percentage for the waypoint image.
     *
     * @param scalePercent The percentage to scale the image (e.g., 50.0 for
     * 50%).
     */
    public void setScalePercent(double scalePercent) {
        if (scalePercent <= 0) {
            throw new IllegalArgumentException("Scale percentage must be greater than 0");
        }
        this.scalePercent = scalePercent;
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint w) {
        if (img == null) {
            return;
        }

        Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());

        // Calculate scaled dimensions
        int scaledWidth = (int) (img.getWidth() * scalePercent / 100.0);
        int scaledHeight = (int) (img.getHeight() * scalePercent / 100.0);

        // Ensure dimensions are at least 1 pixel
        if (scaledWidth < 1) {
            scaledWidth = 1;
        }
        if (scaledHeight < 1) {
            scaledHeight = 1;
        }

        // Center the scaled image
        int x = (int) point.getX() - scaledWidth / 2;
        int y = (int) point.getY() - scaledHeight;

        // Enable high-quality rendering
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the scaled image
        g.drawImage(img, x, y, scaledWidth, scaledHeight, null);
    }
}
