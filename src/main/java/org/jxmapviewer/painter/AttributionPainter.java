package org.jxmapviewer.painter;

import org.jxmapviewer.JXMapViewer;

import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.jxmapviewer.Globals;

/*
 * Created 12/20/22 by Michał Szwaczko (mikey@wirelabs.net)
 */
public class AttributionPainter extends AbstractPainter<JXMapViewer> {

    // defaults
    private Font font = new Font("Dialog", Font.BOLD, 10);
    private Color backgroundColor = Globals.COLOR_BLUE;
    private Color fontColor = Color.WHITE;
    private int horizontal = SwingConstants.LEFT;
    private int vertical = SwingConstants.TOP;
    private int margin = 5;

    @Override
    protected void doPaint(Graphics2D graphics, JXMapViewer mapViewer, int width, int height) {

        // get attribution text
        String attributionText = "Map: " + mapViewer.getTileFactory().getInfo().getAttribution() +
                "  Licence: " + mapViewer.getTileFactory().getInfo().getLicense();

        // set font and calculate attribution text bounding box
        graphics.setFont(font);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle2D textBounds = fontMetrics.getStringBounds(attributionText, graphics);
        int attributionWidth = (int) textBounds.getWidth() + margin*2;
        int attributionHeight = (int) textBounds.getHeight() + margin*2;

        // apply position
        Point position = getPosition(width, height, attributionWidth, attributionHeight);
        // draw container frame
        graphics.setColor(backgroundColor);
        graphics.fillRect(position.x, position.y, attributionWidth, attributionHeight);
        //graphics.setColor(Color.BLACK);
        //graphics.drawRect(position.x, position.y, attributionWidth, attributionHeight);
        // paint string
        graphics.setColor(fontColor);
        int y =(int)(attributionHeight/2.0-fontMetrics.getHeight()/2.0);
        graphics.drawString(attributionText, position.x+margin, position.y+(int)(attributionHeight/2.0)+y);
    }

    private Point getPosition(int width, int height, int attributionWidth, int attributionHeight) {
        int x = 0;
        int y = 0;
        
        if (vertical == SwingConstants.TOP) {
            y = margin;
        }
        if (vertical == SwingConstants.BOTTOM) {
            y = height - attributionHeight - margin;
        }
        if (horizontal == SwingConstants.LEFT) {
            x = margin;
        }
        if (horizontal == SwingConstants.RIGHT) {
            x = width - attributionWidth - margin;
        }

        return new Point(x, y);
    }

    /**
     * Set margin around the attribution text in pixels
     * @param margin margin
     */
    public void setMargin(int margin) {
        this.margin = margin;
    }

    /**
     * Set backround color of the attribution box
     * @param backgroundColor color, default white
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Set font color of the attribution text
     * @param fontColor color, default black
     */
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * Set font of the attribution text
     * @param font font, default Dialog,10px
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Set position of the attribution box
     * should be SwingConstants.(TOP,BOTTOM,LEFT,RIGHT)
     *
     * @param vertical vertical position
     * @param horizontal horizontal position
     */
    public void setPosition(int vertical, int horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }
}
