package com.company.utilities;

import com.company.Product;
import com.company.inventory.TableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static java.util.Objects.isNull;

public class MyTableChangeListener extends DefaultTableCellRenderer implements TableModelListener {
    protected JTable tbl;
    protected ArrayList<Product> products;
    protected Callable callback;

    public MyTableChangeListener(JTable tbl, ArrayList<Product> products) {
        this.tbl = tbl;
        this.products = products;
//        this.callback = callback;

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int columnOne = e.getColumn();
        int rowOne = e.getFirstRow();

        tbl.getColumnModel().getColumn(columnOne).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 table.getValueAt(row, column).toString();

                return c;
            }
        });
    }

}

