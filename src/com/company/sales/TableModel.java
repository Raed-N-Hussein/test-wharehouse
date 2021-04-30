package com.company.sales;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {
    public TableModel(Object[] columns) {
        super(null, columns);
    }
    @Override
    public Class<?> getColumnClass(int column) {
        return switch (column) {
            case 1 -> String.class;
            case 5 -> Boolean.class;
            default -> Integer.class;
        };
    }
}
