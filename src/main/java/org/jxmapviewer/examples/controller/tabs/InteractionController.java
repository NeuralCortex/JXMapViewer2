package org.jxmapviewer.examples.controller.tabs;

import org.jxmapviewer.examples.adapter.SelectionAdapter;
import org.jxmapviewer.examples.painter.SelectionPainter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.examples.LatLonRenderer;
import org.jxmapviewer.examples.LayoutFunctions;
import org.jxmapviewer.examples.controller.MainController;
import org.jxmapviewer.examples.controller.PopulateInterface;
import org.jxmapviewer.examples.model.PositionTableModel;
import org.jxmapviewer.examples.pojo.TableHeaderPOJO;
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
public class InteractionController extends JPanel implements ActionListener, PopulateInterface {

    private final MainController mainController;

    private JXMapViewer mapViewer;
    private final List<Painter<JXMapViewer>> painters = new ArrayList<>();
    private JPanel toolBar;
    private JLabel jlStatus;
    private JButton btnGen;
    private JTable tablePos;
    private JComboBox<Integer> cbNumber;
    private PositionTableModel positionTableModel;
    private SelectionAdapter sa;
    private SelectionPainter sp;

    private final int START = 25;
    private final int STEPPING = 10;

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

        cbNumber = new JComboBox<>();
        int number = START;
        for (int i = 0; i < 3; i++) {
            cbNumber.addItem(i == 0 ? number : (number += STEPPING));
        }
        cbNumber.addActionListener(this);

        toolBar = LayoutFunctions.createOptionPanelX(Globals.COLOR_INDIGO, jlStatus, new JLabel("Number of locations"), cbNumber, btnGen);
        List<TableHeaderPOJO> headerList = new ArrayList<>();
        headerList.add(new TableHeaderPOJO("Idx", String.class));
        headerList.add(new TableHeaderPOJO("Latitude", String.class));
        headerList.add(new TableHeaderPOJO("Longitude", String.class));

        positionTableModel = new PositionTableModel(headerList, new ArrayList<>());
        tablePos = new JTable(positionTableModel);
        tablePos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        LatLonRenderer latLonRenderer = new LatLonRenderer(4);
        tablePos.getColumnModel().getColumn(PositionTableModel.COL.LAT.getIndex()).setCellRenderer(latLonRenderer);
        tablePos.getColumnModel().getColumn(PositionTableModel.COL.LON.getIndex()).setCellRenderer(latLonRenderer);

        tablePos.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            // Check if the selection adjustment is complete
            if (!e.getValueIsAdjusting()) {
                int selected = tablePos.getSelectedRow();
                sp.setSelIndex(selected);
                mapViewer.repaint();
            }
        });

        JScrollPane scroll = new JScrollPane(tablePos);
        scroll.setPreferredSize(new Dimension(250, 1000));

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

       
        mapViewer.addMouseMotionListener(MainController.getPositionListener(mapViewer));
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

    private void reset() {
        List<GeoPosition> positions = generateRandomGeoPositionsInLondon(Integer.parseInt(cbNumber.getSelectedItem().toString()));
        positionTableModel.clear();
        sa.setList(positions);
        sp.setList(positions);
        mapViewer.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnGen)) {
            reset();
        }
        if (e.getSource().equals(cbNumber)) {
            reset();
        }
    }

    @Override
    public void populate() {
        List<GeoPosition> positions = generateRandomGeoPositionsInLondon(Integer.parseInt(cbNumber != null ? cbNumber.getSelectedItem().toString() : START + ""));

        sa = new SelectionAdapter(mapViewer, positions);
        sa.setSelectionAdapterListener((sel) -> {
            jlStatus.setText(sa.getSelection().size() + " Locations Selected");

            positionTableModel.clear();

            for (int i = 0; i < sel.size(); i++) {
                GeoPosition pos = sel.get(i);
                positionTableModel.add(new GeoPosition(pos.getLatitude(), pos.getLongitude()));
            }
        });
        sp = new SelectionPainter(sa, positions);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);

        painters.add(sp);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    @Override
    public void clear() {
        painters.clear();
        positionTableModel.clear();
    }
}
