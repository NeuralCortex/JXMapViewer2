package org.jxmapviewer.viewer;

import java.awt.Color;

/**
 * @author Neural Cortex
 */
public class GeoRectangle {

    private final GeoPosition bottomLeft;
    private final GeoPosition topRight;
    private final Color color;

    public GeoRectangle(GeoPosition bottomLeft, GeoPosition topRight) {
        this(bottomLeft, topRight, Color.BLACK);
    }

    public GeoRectangle(GeoPosition bottomLeft, GeoPosition topRight, Color color) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.color = color;
    }

    public GeoPosition getBottomLeft() {
        return bottomLeft;
    }

    public GeoPosition getTopRight() {
        return topRight;
    }

    public Color getColor() {
        return color;
    }

}
