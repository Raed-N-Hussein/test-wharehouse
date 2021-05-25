package com.company.sales;

import com.company.Product;

import javax.swing.table.DefaultTableModel;
import java.util.List;

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
    @Override
    public boolean isCellEditable(int row, int column) {
        return  column == 2 || column == 3 || column == 4 || column == 5 ;
    }


}
