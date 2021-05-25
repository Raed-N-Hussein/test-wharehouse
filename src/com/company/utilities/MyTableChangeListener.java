package com.company.utils;

import com.company.Product;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static java.util.Objects.isNull;

public class SalesTableChangeListener implements TableModelListener {
    protected JTable tbl;
    protected ArrayList<Product> products;
    protected Callable callback;

    public SalesTableChangeListener(JTable tbl, ArrayList<Product> products, Callable callback) {
        this.tbl = tbl;
        this.products = products;
        this.callback = callback;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        try {
            if (e.getColumn() < 0) return;
            switch (e.getColumn()) {
                case 3:
                    Object value=tbl.getValueAt(e.getFirstRow(),e.getColumn());
                    if (isNull(value)){
                        return;
                    }
                    int newPrice = (Integer) value;
                    products.get(e.getFirstRow()).updateQuantityBasedOnPrice(newPrice);
                    break;
                case 4:
                    int newQuantity;
                    try {
                        newQuantity = (Integer) tbl.getValueAt(e.getFirstRow(), e.getColumn());
                        if (newQuantity > 0) {
                            products.get(e.getFirstRow()).setQuantity(newQuantity);
                            break;
                        }
                        throw new Exception("invalid quantity");
                    } catch (Exception exp) {
                        products.remove(e.getFirstRow());
                    }
                    break;
                case 5:
                    Product product = products.get(e.getFirstRow());
                    product.setCumulative(!product.getCumulative());
                    break;
            }
            this.callback.call();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

