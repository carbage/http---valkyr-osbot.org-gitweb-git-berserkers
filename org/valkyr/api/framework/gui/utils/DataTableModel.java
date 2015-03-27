package org.valkyr.api.framework.gui.utils;

/**
 * @Author Josef
 */

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Vector;

@SuppressWarnings("serial")
public class DataTableModel extends AbstractTableModel implements TableModel {
    // Row<Column>
    private final Vector<String> data = new Vector<String>();
    private final Vector<Returnable<?>> values = new Vector<Returnable<?>>();
    private final String[] COLUMN_TITLES = {"", ""};

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_TITLES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_TITLES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return data.get(rowIndex);
            case 1:
                return values.get(rowIndex);
        }
        return null;
    }

    public Vector<String> getLabels() {
        return data;
    }

    public Vector<Returnable<?>> getValues() {
        return values;
    }
}
