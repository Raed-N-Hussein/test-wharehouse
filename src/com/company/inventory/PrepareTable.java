package com.company.inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrepareTable extends JTable {
    public PrepareTable(DefaultTableModel model) {
        super(model);
    }

    public PrepareTable() {
        super();
    }

    @Override // don't forget this!
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
        Component component = super.prepareRenderer(renderer, rowIndex, columnIndex);
        // this will likely set the whole row. If you only want to set only a specific cell, then
        // you'll need to first check the columnIndex.
//                component.setOpaque(true);


        JComponent jComponent = (JComponent) component;

        jComponent.setBackground(getBackground());
        jComponent.setForeground(getForeground());
        int modelRow = convertRowIndexToModel(rowIndex);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        date.setLenient(false);

//                if (isCellEditable(rowIndex, columnIndex)) {
        Date expiry = null;
        try {
            //there was error not use sting.valueOf() insteadof string only...
            expiry = date.parse(String.valueOf(getValueAt(modelRow, 2)));
        } catch (ParseException e) {
            e.printStackTrace();

        }

        assert expiry != null;
        boolean expired = expiry.after(new Date()) || expiry.equals(new Date());
        int cumulativeQuantity = Integer.parseInt(String.valueOf(getValueAt(modelRow, 5)));

        if (!expired || cumulativeQuantity == 0) {

            jComponent.setBackground(Color.RED);
            jComponent.setForeground(Color.white);

        }
        if (isRowSelected(rowIndex)) {
            jComponent.setBackground(Color.blue);
            jComponent.setForeground(Color.WHITE);
        }


        return component;
    }


}
