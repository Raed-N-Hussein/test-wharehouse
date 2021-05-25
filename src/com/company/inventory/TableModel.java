package com.company.inventory;

import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {
    public TableModel(Object[] columns) {

        super(null, columns);
    }
    @Override
    public Class<?> getColumnClass(int column) {
        return switch (column) {
            case 2 -> DateTableEditor.class;
            case 1,8 -> String.class;
            default -> Integer.class;
        };
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1 || column == 2 || column == 3 || column == 4 || column == 5 || column == 6 || column == 7;
    }

}
