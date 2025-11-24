package org.jxmapviewer.examples;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Locale;
import org.jxmapviewer.Globals;

public class LatLonRenderer extends DefaultTableCellRenderer {

    private final String format;

    public LatLonRenderer(int decimalPoints) {
        this.format = "%." + decimalPoints + "f";
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Number) {
            String formatted = String.format(Globals.DEFAULT_LOCALE, format, value);
            setText(formatted);
            setHorizontalAlignment(JTextField.RIGHT);
        } else if (value instanceof String) {
            try {
                double num = Double.parseDouble((String) value);
                String formatted = String.format(Globals.DEFAULT_LOCALE, format, num);
                setText(formatted);
                setHorizontalAlignment(JTextField.RIGHT);
            } catch (NumberFormatException e) {
                setHorizontalAlignment(JTextField.LEFT);
            }
        } else {
            setHorizontalAlignment(JTextField.LEFT);
        }

        return this;
    }
}
