package org.jxmapviewer.examples.controller;

import java.awt.BorderLayout;
import java.text.MessageFormat;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jxmapviewer.Globals;
import org.jxmapviewer.examples.HelperFunctions;
import org.jxmapviewer.examples.LayoutFunctions;
import org.jxmapviewer.examples.controller.tabs.InteractionController;
import org.jxmapviewer.examples.controller.tabs.MapkitController;
import org.jxmapviewer.examples.controller.tabs.PanZoomController;
import org.jxmapviewer.examples.controller.tabs.SelectionController;
import org.jxmapviewer.examples.controller.tabs.TilesetController;
import org.jxmapviewer.examples.controller.tabs.WaypointController;

/**
 * @author Neural Cortex
 */
public class MainController extends JPanel {

    private final JFrame frame;
    private JTabbedPane tabbedPane;
    private JPanel panelStatus;
    private JLabel labelAbout;
    private JLabel labelStatus;

    public MainController(JFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        String about = MessageFormat.format("Copyright (c) {0} Neural Cortex", String.format("%d", LocalDate.now().getYear()));
        labelAbout = new JLabel(about);
        labelStatus = new JLabel("");

        panelStatus = LayoutFunctions.createOptionPanelX(Globals.COLOR_INDIGO, labelStatus, labelAbout);
        add(panelStatus, BorderLayout.SOUTH);

        frame.add(this);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        HelperFunctions.addTab(tabbedPane, new PanZoomController(this), "Pan and Zoom");
        HelperFunctions.addTab(tabbedPane, new InteractionController(this), "Interaction");
        HelperFunctions.addTab(tabbedPane, new SelectionController(this), "Selection");
        HelperFunctions.addTab(tabbedPane, new WaypointController(this), "Waypoint");
        HelperFunctions.addTab(tabbedPane, new TilesetController(this), "Tilesets");
        HelperFunctions.addTab(tabbedPane, new MapkitController(this), "Mapkit");
    }

    public JLabel getLabelStatus() {
        return labelStatus;
    }
}
