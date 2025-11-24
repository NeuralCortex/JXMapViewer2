package org.jxmapviewer.examples.controller.tabs;

import org.jxmapviewer.examples.painter.CenterPainter;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.examples.StatusBar;
import org.jxmapviewer.examples.controller.MainController;
import org.jxmapviewer.examples.controller.PopulateInterface;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author Neural Cortex
 */
public class PanZoomController extends JPanel implements PopulateInterface {

    private final MainController mainController;

    private JXMapViewer mapViewer;
    private final List<Painter<JXMapViewer>> painters = new ArrayList<>();
    private StatusBar toolBar;

    public PanZoomController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        initOSM();

        toolBar = new StatusBar(Globals.COLOR_INDIGO, 10);

        add(toolBar, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);

        mapViewer.addPropertyChangeListener("zoom", ((evt) -> {
            updateBar(toolBar, mapViewer);
        }));

        mapViewer.addPropertyChangeListener("center", ((evt) -> {
            updateBar(toolBar, mapViewer);
        }));

        updateBar(toolBar, mapViewer);
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

        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        // Set the focus
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        mapViewer.addMouseMotionListener(MainController.getPositionListener(mapViewer));

        painters.add(new CenterPainter());
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    private void updateBar(StatusBar bar, JXMapViewer mapViewer) {
        double lat = mapViewer.getCenterPosition().getLatitude();
        double lon = mapViewer.getCenterPosition().getLongitude();
        int zoom = mapViewer.getZoom();

        bar.getLabel().setText(String.format(Globals.DEFAULT_LOCALE, "Center: Latitude %.4f / Longitude %.4f - Zoom: %d", lat, lon, zoom));
    }

    @Override
    public void populate() {

    }

    @Override
    public void clear() {

    }
}
