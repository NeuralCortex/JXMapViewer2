package org.jxmapviewer.examples.controller.tabs;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.examples.LayoutFunctions;
import org.jxmapviewer.examples.controller.MainController;
import org.jxmapviewer.listener.MousePositionListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 * @author Neural Cortex
 */
public class MapkitController extends JPanel {

    private final MainController mainController;

    private JPanel toolBar;
    private JXMapKit mapKit;

    public MapkitController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        initOSM();

        toolBar = LayoutFunctions.createOptionPanelX(Globals.COLOR_INDIGO, new JLabel(""), new JLabel(""));

        add(toolBar, BorderLayout.NORTH);
        add(mapKit, BorderLayout.CENTER);
    }

    private void initOSM() {
        mapKit = new JXMapKit();

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapKit.setTileFactory(tileFactory);

        //Location of Java
        GeoPosition gp = new GeoPosition(-7.502778, 111.263056);

        mapKit.setZoom(11);
        mapKit.setAddressLocation(gp);

        MousePositionListener mousePositionListener = new MousePositionListener(mapKit.getMainMap());
        mousePositionListener.setGeoPosListener((GeoPosition geoPosition) -> {
            String lat = String.format("%.5f", geoPosition.getLatitude());
            String lon = String.format("%.5f", geoPosition.getLongitude());
            mainController.getLabelStatus().setText("Latitude: " + lat + " Longitude: " + lon);
        });
        mapKit.getMainMap().addMouseMotionListener(mousePositionListener);
    }

}
