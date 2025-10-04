package org.jxmapviewer;

import java.awt.Color;
import javax.swing.JFrame;
import org.jxmapviewer.examples.HelperFunctions;

/**
 * @author Neural Cortex
 */
public class Globals {

    // aspect ratio 16:9
    public static final int HEIGHT = 720;
    public static final int WIDTH = (int) (HEIGHT * 18.0 / 9.0);
    public static final boolean MAXIMIZED = false;
    public static final int FRAME_STATE = MAXIMIZED ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL;

    public static final Color COLOR_BLUE = HelperFunctions.getColorFromHex("#2196F3");
    public static final Color COLOR_INDIGO = HelperFunctions.getColorFromHex("#3f51b5");
    public static final Color COLOR_MAP_BACKGROUND = new Color(44, 44, 46);
    public static final Color COLOR_LOADING_BACKGROUND = new Color(44, 44, 46);

    public static final String PATH_LOG4J2 = "config/log4j2.xml";
    public static final String PATH_IMG_MINUS = "/images/minus.png";
    public static final String PATH_IMG_PLUS = "/images/plus.png";
    public static final String PATH_IMG_LOADING = "/images/loading.png";
    public static final String PATH_IMG_WAYPOINT = "/images/waypoint.png";

    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static final int INPUT_BUFFER_SIZE = 16 * 1024;//16KB
}
