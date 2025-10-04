package org.jxmapviewer.examples.controller.tabs;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.WMSTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.examples.LayoutFunctions;
import org.jxmapviewer.examples.controller.MainController;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.listener.MousePositionListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author Neural Cortex
 */
public class TilesetController extends JPanel {

    private final MainController mainController;

    private JXMapViewer mapViewer;
    private JPanel toolBar;
    private JComboBox<String> cbTiles;
    private final JLabel labelAttr = new JLabel();

    public TilesetController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        initOSM();

        final List<TileFactory> factories = new ArrayList<>();

        TileFactoryInfo osmInfo = new OSMTileFactoryInfo();
        TileFactoryInfo veInfo = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        TileFactoryInfo vesInfo = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
        TileFactoryInfo vehInfo = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);

        // Configure WMSTileFactoryInfo (example using a public WMS server)
        String wmsUrl = "https://ows.terrestris.de/osm/service"; // Example WMS server
        WMSTileFactoryInfo wmsInfo = new WMSTileFactoryInfo(
                0, // minZoom
                14, // maxZoom
                14, // totalMapZoom
                wmsUrl, // baseURL
                "OSM-WMS", // layers
                "", // styles
                "0xAFDAF6", // defaultBgColor
                "image/png", // tileFormat
                "EPSG:4326", // srs
                256 // tileSize
        );

        factories.add(new DefaultTileFactory(osmInfo));
        factories.add(new DefaultTileFactory(veInfo));
        factories.add(new DefaultTileFactory(vesInfo));
        factories.add(new DefaultTileFactory(vehInfo));
        factories.add(new DefaultTileFactory(wmsInfo));

        cbTiles = new JComboBox<>();
        for (int i = 0; i < factories.size(); i++) {
            cbTiles.addItem(factories.get(i).getInfo().getName());
        }
        cbTiles.addItemListener((ItemEvent e) -> {
            TileFactory factory = factories.get(cbTiles.getSelectedIndex());
            TileFactoryInfo info = factory.getInfo();
            mapViewer.setTileFactory(factory);
            labelAttr.setText(info.getAttribution() + " - " + info.getLicense());
        });
        toolBar = LayoutFunctions.createOptionPanelX(Globals.COLOR_INDIGO, new JLabel(""), cbTiles);

        labelAttr.setText(osmInfo.getAttribution() + " - " + osmInfo.getLicense());

        add(toolBar, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);
    }

    private void initOSM() {
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        GeoPosition spain = new GeoPosition(40.413, -3.757);

        // Set the focus
        mapViewer.setZoom(13);
        mapViewer.setAddressLocation(spain);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        MousePositionListener mousePositionListener = new MousePositionListener(mapViewer);
        mousePositionListener.setGeoPosListener((GeoPosition geoPosition) -> {
            String lat = String.format("%.5f", geoPosition.getLatitude());
            String lon = String.format("%.5f", geoPosition.getLongitude());
            mainController.getLabelStatus().setText("Latitude: " + lat + " Longitude: " + lon);
        });
        mapViewer.addMouseMotionListener(mousePositionListener);

        mapViewer.setLayout(new BorderLayout());
        mapViewer.add(labelAttr, BorderLayout.SOUTH);
    }
}
