package org.jxmapviewer;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

public class JXMapViewerLogger {

    private static final Logger logger = LogManager.getLogger(JXMapViewerLogger.class);

    public static void initLogger(String path) {
        try {
            URL configUrl = JXMapViewerLogger.class.getClassLoader().getResource(path);
            URI configUri = configUrl.toURI();
            LoggerContext loggerContext = (LoggerContext) LoggerContext.getContext(false);
            loggerContext.setConfigLocation(configUri);
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage());
        }
    }
}
