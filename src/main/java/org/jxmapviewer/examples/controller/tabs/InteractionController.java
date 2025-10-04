package org.jxmapviewer.examples.controller.tabs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
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
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author Neural Cortex
 */
public class InteractionController extends JPanel implements ActionListener {

    private final MainController mainController;

    private JXMapViewer mapViewer;
    private final List<Painter<JXMapViewer>> painters = new ArrayList<>();
    private JPanel toolBar;
    private JLabel jlStatus;
    private JButton btnGen;
    private JList<String> jList;
    private DefaultListModel<String> listModel;
    private SelectionAdapter sa;
    private SelectionPainter sp;

    private final int LOCATIONS = 25;

    public InteractionController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        initOSM();

        jlStatus = new JLabel();
        jlStatus.setText("0 locations selected");
        btnGen = new JButton("Generate random locations");
        btnGen.addActionListener(this);
        toolBar = LayoutFunctions.createOptionPanelX(Globals.COLOR_INDIGO, jlStatus, btnGen);
        listModel = new DefaultListModel<>();
        jList = new JList<>(listModel);

        jList.addListSelectionListener((ListSelectionEvent e) -> {
            // Check if the selection adjustment is complete
            if (!e.getValueIsAdjusting()) {
                int selected = jList.getSelectedIndex();
                sp.setSelIndex(selected);
                mapViewer.repaint();
            }
        });

        JScrollPane scroll = new JScrollPane(jList);
        scroll.setPreferredSize(new Dimension(150, 1000));

        add(toolBar, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);
        add(scroll, BorderLayout.EAST);
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

        GeoPosition london = new GeoPosition(51.4895, -0.0277);

        // Set the focus
        mapViewer.setZoom(9);
        mapViewer.setAddressLocation(london);

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

        List<GeoPosition> positions = generateRandomGeoPositionsInLondon(LOCATIONS);

        sa = new SelectionAdapter(mapViewer, positions);
        sa.setSelectionAdapterListener((sel) -> {
            jlStatus.setText(sa.getSelection().size() + " Locations Selected");

            listModel.clear();

            for (int i = 0; i < sel.size(); i++) {
                GeoPosition pos = sel.get(i);
                String lat = String.format("%.4f", pos.getLatitude());
                String lon = String.format("%.4f", pos.getLongitude());
                listModel.add(i, i + " (" + lat + " , " + lon + ")");
            }
        });
        sp = new SelectionPainter(sa, positions);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);

        painters.add(sp);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    private List<GeoPosition> generateRandomGeoPositionsInLondon(int count) {
        List<GeoPosition> positions = new ArrayList<>();
        Random random = new Random();

        // Bounding box for London
        double minLat = 51.3; // Southern boundary
        double maxLat = 51.7; // Northern boundary
        double minLon = -0.5; // Western boundary
        double maxLon = 0.1;  // Eastern boundary

        for (int i = 0; i < count; i++) {
            // Generate random latitude and longitude within bounds
            double lat = minLat + (maxLat - minLat) * random.nextDouble();
            double lon = minLon + (maxLon - minLon) * random.nextDouble();
            positions.add(new GeoPosition(lat, lon));
        }

        return positions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnGen)) {
            List<GeoPosition> positions = generateRandomGeoPositionsInLondon(LOCATIONS);
            listModel.clear();
            sa.setList(positions);
            sp.setList(positions);
            mapViewer.repaint();
        }
    }
}
