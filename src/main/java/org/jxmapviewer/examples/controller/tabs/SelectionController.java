package org.jxmapviewer.examples.controller.tabs;

import org.jxmapviewer.examples.adapter.RectangleAdapter;
import org.jxmapviewer.examples.painter.RectanglePainter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import org.jxmapviewer.examples.controller.PopulateInterface;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.listener.MousePositionListener;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.GeoRectangle;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author Neural Cortex
 */
public class SelectionController extends JPanel implements ActionListener, PopulateInterface {

    private final MainController mainController;

    private JXMapViewer mapViewer;
    private final List<Painter<JXMapViewer>> painters = new ArrayList<>();
    private JPanel toolBar;
    private JButton btnGen;
    private RectangleAdapter ra;
    private RectanglePainter rp;

    private final int LOCATIONS = 10;

    public SelectionController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        initOSM();

        btnGen = new JButton("Generate random rectangles");
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

        GeoPosition paris = new GeoPosition(48.8566, 2.3522);

        // Set the focus
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(paris);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        mapViewer.addMouseMotionListener(MainController.getPositionListener(mapViewer));
    }

    public List<GeoRectangle> generateRandomRectanglesInParis(int count) {
        List<GeoRectangle> rectangles = new ArrayList<>();
        Random random = new Random();

        // Bounding box for Paris
        double minLat = 48.8156; // Southern boundary
        double maxLat = 48.9022; // Northern boundary
        double minLon = 2.2247;  // Western boundary
        double maxLon = 2.4699;  // Eastern boundary

        for (int i = 0; i < count; i++) {
            // Generate random bottom-left corner
            double lat1 = minLat + (maxLat - minLat) * random.nextDouble();
            double lon1 = minLon + (maxLon - minLon) * random.nextDouble();

            // Generate random top-right corner, ensuring it's above and to the right
            double lat2 = lat1 + (maxLat - lat1) * random.nextDouble();
            double lon2 = lon1 + (maxLon - lon1) * random.nextDouble();

            // Ensure minimum rectangle size (0.001 degrees) and valid ordering
            lat2 = Math.max(lat2, lat1 + 0.001); // Ensure top-right latitude > bottom-left
            lon2 = Math.max(lon2, lon1 + 0.001); // Ensure top-right longitude > bottom-left
            lat2 = Math.min(lat2, maxLat); // Keep within bounds
            lon2 = Math.min(lon2, maxLon);

            // Generate random color
            Color randomColor = new Color(
                    random.nextInt(256), // Red
                    random.nextInt(256), // Green
                    random.nextInt(256), // Blue
                    255 // Alpha (semi-transparent)
            );

            GeoPosition bottomLeft = new GeoPosition(lat1, lon1);
            GeoPosition topRight = new GeoPosition(lat2, lon2);
            rectangles.add(new GeoRectangle(bottomLeft, topRight, randomColor));
        }

        return rectangles;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnGen)) {
            List<GeoRectangle> rectangles = generateRandomRectanglesInParis(LOCATIONS);
            rp.setList(rectangles);
            ra.setList(rectangles);
            ra.setSelectedRectangle(null);
            mapViewer.repaint();
        }
    }

    @Override
    public void populate() {
        List<GeoRectangle> rectangles = generateRandomRectanglesInParis(LOCATIONS);

        ra = new RectangleAdapter(mapViewer, rectangles);
        rp = new RectanglePainter(ra, rectangles);
        mapViewer.addMouseListener(ra);

        painters.add(rp);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    @Override
    public void clear() {
        painters.clear();
    }
}
