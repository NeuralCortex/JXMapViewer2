package org.jxmapviewer.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class LayoutFunctions {

    public static JPanel createVerticalGridbag(JComponent... components) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel panel = new JPanel();
        gridBagLayout.setConstraints(panel, gridBagConstraints);
        panel.setLayout(gridBagLayout);

        for (int i = 0; i < components.length; i++) {
            JComponent component = components[i];

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;

            if (i < components.length - 1) {
                gridBagConstraints.insets = new Insets(0, 0, 10, 0);
            } else {
                gridBagConstraints.insets = new Insets(0, 0, 0, 0);
            }

            if (component instanceof JTable) {
                JScrollPane scrollPane = new JScrollPane(component, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.fill = GridBagConstraints.BOTH;

                panel.add(scrollPane, gridBagConstraints);
            } else {
                gridBagConstraints.weightx = 0.0;
                gridBagConstraints.weighty = 0.0;
                gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

                panel.add(component, gridBagConstraints);
            }
        }

        return panel;
    }

    public static JPanel createOptionPanelX(Color backGroundColor, JComponent left, JComponent... right) {
        JPanel panel = new JPanel();
        panel.setBackground(backGroundColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        left = colorComponent(backGroundColor, left);

        panel.add(left);

        panel.add(Box.createHorizontalGlue());

        for (int i = 0; i < right.length; i++) {
            JComponent component = right[i];

            component = colorComponent(backGroundColor, component);

            panel.add(Box.createRigidArea(new Dimension(10, 0)));
            panel.add(component);
        }

        return panel;
    }

    public static JPanel createOptionPanelX_Short(Color backGroundColor, JComponent... middle) {
        JPanel panel = new JPanel();
        panel.setBackground(backGroundColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        for (int i = 0; i < middle.length; i++) {
            JComponent component = middle[i];

            component = colorComponent(backGroundColor, component);

            panel.add(Box.createRigidArea(new Dimension(10, 0)));
            panel.add(component);
        }

        return panel;
    }

    public static JPanel createOptionPanelY(Color backGroundColor, JComponent... ver) {
        JPanel panel = new JPanel();
        panel.setBackground(backGroundColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (int i = 0; i < ver.length; i++) {
            JComponent component = ver[i];

            component = colorComponent(backGroundColor, component);

            panel.add(Box.createRigidArea(new Dimension(0, 0)));
            panel.add(component);
        }

        return panel;
    }

    private static JComponent colorComponent(Color backGroundColor, JComponent component) {

        if (component instanceof JLabel) {
            component.setForeground(Color.WHITE);
        }
        if (component instanceof JCheckBox) {
            component.setForeground(Color.WHITE);
            component.setBackground(backGroundColor);
        }
        if (component instanceof JRadioButton) {
            component.setForeground(Color.WHITE);
            component.setBackground(backGroundColor);
        }
        if (component instanceof JComboBox) {
            component.setMaximumSize(component.getPreferredSize());
        }

        return component;
    }
}
