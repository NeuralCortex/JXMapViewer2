package org.jxmapviewer.examples;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jxmapviewer.Globals;
import org.jxmapviewer.JXMapViewerLogger;
import org.jxmapviewer.examples.controller.MainController;

/**
 * @author Neural Cortex
 */
public class Examples {
    
     private static final Logger log = LogManager.getLogger(Examples.class);

    public static void main(String[] args) {
        Examples examples = new Examples();
        examples.init();
    }

    private void init() {
        //do not use in production
        JXMapViewerLogger.initLogger(Globals.PATH_LOG4J2);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }

        JFrame frame = new JFrame();
        frame.setTitle("JXMapViewer2 Examples");
        frame.setIconImage(new ImageIcon(Examples.class.getResource("/images/app.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Globals.WIDTH, Globals.HEIGHT);
        frame.setExtendedState(Globals.FRAME_STATE);

        new MainController(frame);
        
        HelperFunctions.centerWindow(frame);

        frame.setVisible(true);
    }
}
