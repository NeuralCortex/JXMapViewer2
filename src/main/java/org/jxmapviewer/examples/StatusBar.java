package org.jxmapviewer.examples;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Neural Cortex
 */
public class StatusBar extends JPanel {

    private final Color color;
    private final int borderSize;
    private JLabel label;
    private JLabel copyright;

    public StatusBar(Color color, int borderSize) {
        this.color = color;
        this.borderSize = borderSize;
        init();
    }

    private void init() {
        setBackground(color);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
        label = new JLabel();
        copyright = new JLabel();
        label.setForeground(Color.WHITE);
        copyright.setForeground(Color.WHITE);
        add(label);
        add(Box.createHorizontalGlue());
        add(copyright);
    }

    public JLabel getLabel() {
        return label;
    }

    public JLabel getCopyright() {
        return copyright;
    }
}
