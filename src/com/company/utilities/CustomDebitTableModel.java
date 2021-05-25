package com.company.utilities;

import javax.swing.table.DefaultTableModel;

public class CustomDebitTableModel extends DefaultTableModel {
    public CustomDebitTableModel(Object[] headers) {
        super(null, headers);

    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
