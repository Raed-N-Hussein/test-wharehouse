package com.company.inventory;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {
    public TableModel(Object[] columns) {

        super(null, columns);
    }
    @Override
    public Class<?> getColumnClass(int column) {
        return switch (column) {
            case 1, 5 -> String.class;
            default -> Integer.class;
        };
    }
}
