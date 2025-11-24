package org.jxmapviewer.examples.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jxmapviewer.examples.pojo.TableHeaderPOJO;
import org.jxmapviewer.viewer.GeoPosition;

public class PositionTableModel extends AbstractTableModel {

    private static final Logger _log = LogManager.getLogger(PositionTableModel.class);
    private final List<TableHeaderPOJO> headerList;
    private List<GeoPosition> list;

    public static enum COL {
        IDX, LAT, LON;

        public static COL fromIndex(int index) {
            return values()[index];
        }
        
        public int getIndex(){
            return this.ordinal();
        }
    }

    public PositionTableModel(List<TableHeaderPOJO> headerList, List<GeoPosition> list) {
        this.headerList = headerList;
        this.list = list;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return headerList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GeoPosition geoPosition = list.get(rowIndex);
        switch (COL.fromIndex(columnIndex)) {

            case IDX -> {
                return rowIndex;
            }
            case LAT -> {
                return geoPosition.getLatitude();
            }
            case LON -> {
                return geoPosition.getLongitude();
            }
        }
        return geoPosition;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return headerList.get(columnIndex).getCls();
    }

    @Override
    public String getColumnName(int column) {
        return headerList.get(column).getName();
    }

    public void add(GeoPosition geoPosition) {
        list.add(geoPosition);
        fireTableDataChanged();
    }

    public List<GeoPosition> getList() {
        return list;
    }

    public void setList(List<GeoPosition> list) {
        this.list = list;
        fireTableDataChanged();
    }

    public void clear() {
        this.list = new ArrayList<>();
        fireTableDataChanged();
    }
}
