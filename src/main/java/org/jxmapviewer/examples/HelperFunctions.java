package org.jxmapviewer.examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JTabbedPane;

public class HelperFunctions {

    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (dimension.getWidth() - frame.getWidth()) / 2.0;
        double y = (dimension.getHeight() - frame.getHeight()) / 2.0;
        frame.setLocation((int) x, (int) y);
    }

    public static Color getColorFromHex(String hexColor) {
        // Remove the '#' if present
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }

        // Ensure the hex string is valid (6 characters for RGB)
        if (hexColor.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color code. Must be in format #RRGGBB or RRGGBB");
        }

        try {
            // Parse the hex values for red, green, and blue
            int red = Integer.parseInt(hexColor.substring(0, 2), 16);
            int green = Integer.parseInt(hexColor.substring(2, 4), 16);
            int blue = Integer.parseInt(hexColor.substring(4, 6), 16);

            // Return the Color object
            return new Color(red, green, blue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color code. Must contain valid hexadecimal values");
        }
    }

    public static void addTab(JTabbedPane tabbedPane, Component controller, String tabName) {
        long start = System.currentTimeMillis();
        tabbedPane.addTab(tabName, controller);
        long end = System.currentTimeMillis();
        System.out.println("Loadtime (" + controller.toString() + ") in ms: " + (end - start));
    }
}
