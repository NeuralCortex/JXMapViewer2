package org.jxmapviewer.examples.controller.tabs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.examples.LayoutFunctions;
import org.jxmapviewer.examples.controller.MainController;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.listener.MousePositionListener;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.RoutePainter;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 *
 * @author Neural Cortex
 */
public class WaypointController extends JPanel implements ActionListener {

    private final MainController mainController;

    private JXMapViewer mapViewer;
    private final List<Painter<JXMapViewer>> painters = new ArrayList<>();
    private JPanel toolBar;
    private JButton btnGen;

    private final int LOCATIONS = 10;

    public WaypointController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        initOSM();

        btnGen = new JButton("Generate random waypoints");
        btnGen.addActionListener(this);
        toolBar = LayoutFunctions.createOptionPanelX(Globals.COLOR_INDIGO, new JLabel(""), btnGen);

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

        HashMap<String, Object> map = generateRandomWaypoints(LOCATIONS);
        Set<Waypoint> waypoints = (Set<Waypoint>) map.get("waypoints");

        WaypointPainter<Waypoint> wpainter = new WaypointPainter<>();
        wpainter.setWaypoints(waypoints);
        wpainter.setRenderer(new DefaultWaypointRenderer(50.0));
        painters.add(new RoutePainter((List<GeoPosition>) map.get("track")));
        painters.add(wpainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    /**
     * Generates x random waypoints in Spain.
     *
     * @param x Number of waypoints to generate.
     * @return A map containing a List of GeoPositions for the route and a Set
     * of Waypoints for markers.
     * @throws IllegalArgumentException if x < 1.
     */
    private HashMap<String, Object> generateRandomWaypoints(int x) {
        if (x < 1) {
            throw new IllegalArgumentException("x must be at least 1");
        }

        // Spain's bounding box: latitude 36 to 44, longitude -10 to 4
        final double MIN_LAT = 36.0;
        final double MAX_LAT = 44.0;
        final double MIN_LON = -10.0;
        final double MAX_LON = 4.0;

        Random random = new Random();
        List<GeoPosition> track = new ArrayList<>();
        Set<Waypoint> waypoints = new HashSet<>();

        // Generate x unique random waypoints
        for (int i = 0; i < x; i++) {
            // Generate random lat/lon within Spain's bounds
            double lat = random.nextDouble() * (MAX_LAT - MIN_LAT) + MIN_LAT;
            double lon = random.nextDouble() * (MAX_LON - MIN_LON) + MIN_LON;
            // Round to 4 decimal places
            lat = Math.round(lat * 10000.0) / 10000.0;
            lon = Math.round(lon * 10000.0) / 10000.0;
            GeoPosition geoPosition = new GeoPosition(lat, lon);
            track.add(geoPosition);
            waypoints.add(new DefaultWaypoint(geoPosition));
        }

        // Return track and waypoints
        HashMap<String, Object> result = new HashMap<>();
        result.put("track", track);
        result.put("waypoints", waypoints);
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnGen)) {
            painters.clear();

            HashMap<String, Object> map = generateRandomWaypoints(LOCATIONS);
            Set<Waypoint> waypoints = (Set<Waypoint>) map.get("waypoints");

            WaypointPainter<Waypoint> wpainter = new WaypointPainter<>();
            wpainter.setWaypoints(waypoints);
            wpainter.setRenderer(new DefaultWaypointRenderer(50.0));
            painters.add(new RoutePainter((List<GeoPosition>) map.get("track")));
            painters.add(wpainter);

            CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
            mapViewer.setOverlayPainter(painter);
        }
    }
}
