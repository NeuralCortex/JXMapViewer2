package org.jxmapviewer.examples.controller.tabs;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.examples.LayoutFunctions;
import org.jxmapviewer.examples.controller.MainController;
import org.jxmapviewer.examples.controller.PopulateInterface;
import org.jxmapviewer.listener.MousePositionListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 * @author Neural Cortex
 */
public class MapkitController extends JPanel implements PopulateInterface{

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

        mapKit.getMainMap().addMouseMotionListener(MainController.getPositionListener(mapKit.getMainMap()));
    }

    @Override
    public void populate() {
       
    }

    @Override
    public void clear() {
       
    }

}
